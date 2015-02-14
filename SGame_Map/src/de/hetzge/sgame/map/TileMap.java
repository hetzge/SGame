package de.hetzge.sgame.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_XY;
import de.hetzge.sgame.map.tmx.TMXMap;
import de.hetzge.sgame.map.tmx.TMXMap.Layer;
import de.hetzge.sgame.render.IF_RenderableLoader;
import de.hetzge.sgame.render.PredefinedRenderId;

public class TileMap implements IF_Map, Serializable {

	public class Tile implements IF_XY {

		// Rectangle interface auch verwenden

		private final int tileId;

		private final int x;
		private final int y;

		public Tile(int x, int y, int tileId) {
			this.tileId = tileId;
			this.x = x;
			this.y = y;
		}

		public int getRenderId() {
			return TileMap.this.getRenderId(this.tileId);
		}

		@Override
		public float getX() {
			return this.x * TileMap.this.tileSize + this.getWidth() / 2;
		}

		@Override
		public float getY() {
			return this.y * TileMap.this.tileSize + this.getHeight() / 2;
		}

		public float getWidth() {
			return TileMap.this.tileSize;
		}

		public float getHeight() {
			return TileMap.this.tileSize;
		}

	}

	private class MapCollision implements IF_Collision, Serializable {

		@Override
		public int getWidthInTiles() {
			return TileMap.this.widthInTiles;
		}

		@Override
		public int getHeightInTiles() {
			return TileMap.this.heightInTiles;
		}

		@Override
		public boolean isCollision(int x, int y) {
			return this.isFixedCollision(x, y) || this.isFlexibleCollision(x, y);
		}

		public boolean isFixedCollision(int x, int y) {
			int tileX = (x - (x % TileMap.this.collisionTileFactor)) / TileMap.this.collisionTileFactor;
			int tileY = (y - (y % TileMap.this.collisionTileFactor)) / TileMap.this.collisionTileFactor;

			// TODO resolve tile and tile collision
			// boolean isMapCollision =
			// TileMap.this.tiles[tileX][tileY].tileDefinition.getCollision().isCollision(x
			// % TileMap.this.collisionTileFactor, y %
			// TileMap.this.collisionTileFactor);
			// if (isMapCollision)
			// return true;

			Collection<Boolean> flexibleConnectedObjects = TileMap.this.fixEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : flexibleConnectedObjects) {
				if (aBoolean) {
					return true;
				}
			}
			return false;
		}

		public boolean isFlexibleCollision(int x, int y) {
			Collection<Boolean> fixedConnectedObjects = TileMap.this.flexibleEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : fixedConnectedObjects) {
				if (aBoolean) {
					return true;
				}
			}

			return false;
		}

		@Override
		public void setCollision(int x, int y, boolean collision) {
			throw new IllegalAccessError("Don't set collision direct on map");
		}

	}

	private final Tile[][] tiles;
	private final int tileSize;
	private final int collisionTileFactor;
	private final int widthInTiles;
	private final int heightInTiles;
	private final MapCollision mapCollision;
	private final ActiveCollisionMap fixEntityCollisionMap;
	private final ActiveCollisionMap flexibleEntityCollisionMap;
	private final TMXMap tmxMap;
	private int[] renderIdByTileId;

	public TileMap(MapConfig mapConfig) {
		this(mapConfig.pathToMapJson);
	}

	private TileMap(int widthInTiles, int heightInTiles) {
		this.tmxMap = null;
		this.tileSize = 32;
		this.collisionTileFactor = 3;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.tiles = new TileMap.Tile[widthInTiles][heightInTiles];

		Stopwatch stopwatch = new Stopwatch("init tile map tiles array");
		for (int x = 0; x < widthInTiles; x++) {
			for (int y = 0; y < heightInTiles; y++) {
				this.tiles[x][y] = new Tile(x, y, 0);
			}
		}
		stopwatch.stop();

		this.mapCollision = new MapCollision();
		this.fixEntityCollisionMap = new ActiveCollisionMap(widthInTiles * this.collisionTileFactor, heightInTiles * this.collisionTileFactor);
		this.flexibleEntityCollisionMap = new ActiveCollisionMap(widthInTiles * this.collisionTileFactor, heightInTiles * this.collisionTileFactor);
	}

	private TileMap(String pathToJsonFile) {
		this.tmxMap = new TMXMap(pathToJsonFile);

		this.widthInTiles = this.tmxMap.getWidth();
		this.heightInTiles = this.tmxMap.getHeight();
		this.tileSize = this.tmxMap.getTileWidth();
		this.collisionTileFactor = 3;
		this.tiles = new TileMap.Tile[this.widthInTiles][this.heightInTiles];

		List<Layer> layers = this.tmxMap.getLayers();
		for (Layer layer : layers) {
			int layerWidth = layer.getWidth();
			int layerHeight = layer.getHeight();
			int layerX = layer.getX();
			int layerY = layer.getY();
			for (int x = layerX; x < layerX + layerWidth; x++) {
				for (int y = layerY; y < layerY + layerHeight; y++) {
					this.tiles[x][y] = new Tile(x, y, layer.getData(x - layerX, y - layerY) - 1);
				}
			}
		}

		this.mapCollision = new MapCollision();
		this.fixEntityCollisionMap = new ActiveCollisionMap(this.widthInTiles * this.collisionTileFactor, this.heightInTiles * this.collisionTileFactor);
		this.flexibleEntityCollisionMap = new ActiveCollisionMap(this.widthInTiles * this.collisionTileFactor, this.heightInTiles * this.collisionTileFactor);
	}

	public void initTIleRenderIds(IF_RenderableLoader renderableLoader) {
		if (this.tmxMap != null) {
			this.renderIdByTileId = renderableLoader.loadTilesets(this.tmxMap.getTilesets());
		}
	}

	private int getRenderId(int tileId) {
		if (this.renderIdByTileId == null || tileId >= this.renderIdByTileId.length) {
			return PredefinedRenderId.DEFAULT;
		}
		return this.renderIdByTileId[tileId];
	}

	public Tile getTile(int x, int y) {
		return this.tiles[x][y];
	}

	@Override
	public int getWidthInTiles() {
		return this.widthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.heightInTiles;
	}

	@Override
	public int getCollisionTileFactor() {
		return 3;
	}

	@Override
	public IF_Collision getCollision() {
		return this.mapCollision;
	}

	@Override
	public ActiveCollisionMap getFixEntityCollisionMap() {
		return this.fixEntityCollisionMap;
	}

	@Override
	public ActiveCollisionMap getFlexibleEntityCollisionMap() {
		return this.flexibleEntityCollisionMap;
	}

	@Override
	public float getTileSize() {
		return this.tileSize;
	}

}
