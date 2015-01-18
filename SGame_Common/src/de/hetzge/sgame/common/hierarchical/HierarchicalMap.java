package de.hetzge.sgame.common.hierarchical;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.DummyMap;
import de.hetzge.sgame.common.IF_XYFunction;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.definition.IF_Map;

public class HierarchicalMap implements Serializable {

	private class ConnectionGroup implements Serializable {
		private final Sector from;
		private final Sector to;
		private final int groupFrom;
		private final int groupTo;

		public ConnectionGroup(Sector from, Sector to, int groupFrom, int groupTo) {
			this.from = from;
			this.to = to;
			this.groupFrom = groupFrom;
			this.groupTo = groupTo;
		}
	}

	private class Connection implements Serializable {
		private final Tile from;
		private final Tile to;

		private final ConnectionGroup connectionGroup;

		public Connection(Tile from, Tile to, ConnectionGroup connectionGroup) {
			this.from = from;
			this.to = to;
			this.connectionGroup = connectionGroup;
		}
	}

	private class Sector implements Serializable {
		private final int subX;
		private final int subY;
		private final int x;
		private final int y;
		private final int width;
		private final int height;

		private final List<Connection> connections = new LinkedList<>();
		private final Tile[][] tiles;

		public Sector(int x, int y, int subX, int subY, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.subX = subX;
			this.subY = subY;

			this.tiles = this.createTiles();
		}

		private Tile[][] createTiles() {
			Tile[][] result = this.recalculateTiles();
			return result;
		}

		private Tile[][] recalculateTiles() {
			Tile[][] result = new Tile[this.width][this.height];
			int group = 0;
			for (int x = 0; x < this.width; x++) {
				for (int y = 0; y < this.height; y++) {
					Tile startFloodTile = result[x][y];
					if (startFloodTile == null) {
						group++;
						final int toUseGroup = group;
						this.floodTilesAndSet(result, x, y, (tileX, tileY) -> {
							Tile tile = result[tileX][tileY];
							if (tile == null) {
								tile = new Tile(toUseGroup, this, tileX, tileY);
								for (Orientation orientation : Orientation.Simple) {
									tile.connections[orientation.ordinal()] = this.connectToNeighbor(orientation, tile);
								}
								return tile;
							}
							return tile;
						});
					}
				}
			}

			return result;
		}

		private Connection connectToNeighbor(Orientation orientation, Tile tile) {

			// check if in the given orienation is the border
			int outsideX = (int) (tile.x + orientation.orientationFactor.getX());
			int outsideY = (int) (tile.y + orientation.orientationFactor.getY());
			if (this.isInsideSector(outsideX, outsideY)) {
				return null;
			}

			int sectorX = (int) (this.subX + orientation.orientationFactor.getX());
			int sectorY = (int) (this.subY + orientation.orientationFactor.getY());

			Orientation oppositOrienation = Orientation.OPPOSITS.get(orientation);

			if (sectorX >= 0 && sectorY >= 0 && sectorX < HierarchicalMap.this.sectors.length && sectorY < HierarchicalMap.this.sectors[0].length) {
				Sector neighbor = HierarchicalMap.this.sectors[sectorX][sectorY];
				if (neighbor != null) {

					int neighborSectorTileX = oppositOrienation.orientationFactor.getX() < 0 ? neighbor.width - 1 : oppositOrienation.orientationFactor.getX() > 0 ? 1 : tile.x;
					int neighborSectorTileY = oppositOrienation.orientationFactor.getY() < 0 ? neighbor.height - 1 : oppositOrienation.orientationFactor.getY() > 0 ? 1 : tile.y;

					Tile neighborTile = neighbor.tiles[neighborSectorTileX][neighborSectorTileY];
					if (neighborTile != null) {
						Connection connection = neighborTile.connections[oppositOrienation.ordinal()];
						if (connection != null) {
							ConnectionGroup connectionGroup = connection.connectionGroup;
							if (connectionGroup == null) {
								connectionGroup = new ConnectionGroup(this, neighbor, tile.group, neighborTile.group);
							}
							return new Connection(tile, neighborTile, connectionGroup);
						}
					}

				}
			}

			return null;
		}

		private void floodTilesAndSet(Tile[][] tiles, int x, int y, IF_XYFunction<Tile> tileFunction) {
			if (this.isInsideSector(x, y) && tiles[x][y] == null) {
				tiles[x][y] = tileFunction.on(x, y);
				if (tiles[x][y] == null) {
					throw new IllegalStateException();
				}

				int nextX;
				int nextY;

				nextX = x + 1;
				nextY = y;
				this.floodTilesAndSet(tiles, nextX, nextY, tileFunction);

				nextX = x - 1;
				nextY = y;
				this.floodTilesAndSet(tiles, nextX, nextY, tileFunction);

				nextX = x;
				nextY = y + 1;
				this.floodTilesAndSet(tiles, nextX, nextY, tileFunction);

				nextX = x;
				nextY = y - 1;
				this.floodTilesAndSet(tiles, nextX, nextY, tileFunction);
			}
		}

		private boolean isSectorBorder(Tile tile) {
			return this.isSectorBorder(tile.x, tile.y);
		}

		private boolean isSectorBorder(int x, int y) {
			return x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1;
		}

		private boolean isInsideSector(int x, int y) {
			return x >= 0 && y >= 0 && x < this.width && y < this.height;
		}

	}

	private class Tile implements Serializable {
		private final int group;
		private final Sector sector;
		private final int x;
		private final int y;

		private Connection[] connections = new Connection[4];

		public Tile(int group, Sector sector, int x, int y) {
			this.group = group;
			this.sector = sector;
			this.x = x;
			this.y = y;
		}

	}

	private transient final IF_Map map;
	private Sector[][] sectors;

	public HierarchicalMap(IF_Map map) {

		this.map = map;
		this.createSubSectors();
	}

	private void createSubSectors() {
		int width = this.map.getWidthInCollisionTiles();
		int height = this.map.getHeightInCollisionTiles();
		int subSectorWidthSize = this.calculateSubSectorSize(width);
		int subSectorHeightSize = this.calculateSubSectorSize(height);
		int subSectorWidthFactor = width / subSectorWidthSize;
		int subSectorHeightFactor = height / subSectorHeightSize;
		if (subSectorWidthSize > 1 && subSectorHeightSize > 1) {
			this.sectors = new Sector[subSectorWidthFactor][subSectorHeightFactor];
			for (int x = 0; x < subSectorWidthFactor; x++) {
				for (int y = 0; y < subSectorHeightFactor; y++) {
					this.sectors[x][y] = new Sector(x * subSectorWidthSize, y * subSectorHeightSize, x, y, subSectorWidthSize, subSectorHeightSize);
				}
			}
		} else {
			throw new IllegalStateException();
		}
	}

	private int calculateSubSectorSize(int size) {
		// if (size % 4 == 0)
		// return size / 4;
		// if (size % 3 == 0)
		// return size / 3;
		// if (size % 2 == 0)
		// return size / 2;
		// return 1;

		// TODO
		return 10;
	}

	public static void main(String[] args) {
		Stopwatch stopwatch = new Stopwatch("HierarchicalMap");
		HierarchicalMap2 hierarchicalMap = new HierarchicalMap2(new DummyMap());
		hierarchicalMap.findPath(10, 10, 75, 75);
		stopwatch.stop();
	}

}
