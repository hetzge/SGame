package de.hetzge.sgame.map;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.ComplexRectangle;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.common.serializer.Serializer;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class TileMap<CONTEXT extends IF_RenderableContext> implements IF_Map, IF_Renderable<IF_RenderableContext>, Serializable {

	private class Tile implements IF_RenderInformation, Serializable, IF_ImmutablePrimitivRectangle {

		// Rectangle interface auch verwenden
		// Renderable key unter in cachen (static hochzählen)

		private final int tileId;

		private final int x;
		private final int y;

		public Tile(int x, int y) {
			this.tileId = 0; // TODO
			this.x = x;
			this.y = y;
		}

		@Override
		public IF_ImmutablePrimitivRectangle getRenderedRectangle() {
			return this;
		}

		@Override
		public int getRenderableKey() {
			return MapConfig.INSTANCE.tilePool.getRenderableId(this.tileId);
		}

		@Override
		public float getX() {
			return this.x * TileMap.this.tileSize;
		}

		@Override
		public float getY() {
			return this.y * TileMap.this.tileSize;
		}

		@Override
		public float getWidth() {
			return TileMap.this.tileSize;
		}

		@Override
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

			Collection<Boolean> flexibleConnectedObjects = TileMap.this.flexibleEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : flexibleConnectedObjects) {
				if (aBoolean)
					return true;
			}
			return false;
		}

		public boolean isFlexibleCollision(int x, int y) {
			Collection<Boolean> fixedConnectedObjects = TileMap.this.fixEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : fixedConnectedObjects) {
				if (aBoolean)
					return true;
			}

			return false;
		}

		@Override
		public void setCollision(int x, int y, boolean collision) {
			throw new IllegalAccessError("Don't set collision direct on map");
		}

	}

	private final Tile[][] tiles;
	private final float tileSize;
	private final int collisionTileFactor;
	private final int widthInTiles;
	private final int heightInTiles;
	private final MapCollision mapCollision;
	private final ActiveCollisionMap fixEntityCollisionMap;
	private final ActiveCollisionMap flexibleEntityCollisionMap;

	public static void main(String[] args) {

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			for (int i = 0; i < 1000; i++) {
				TileMap<IF_RenderableContext> tileMap = new TileMap<>(100, 100, 32, 3);
				fout = new FileOutputStream("cache/" + i + ".map");
				oos = new ObjectOutputStream(fout);
				oos.write(Serializer.toByteArray(tileMap));

				oos.close();
				fout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TileMap(int widthInTiles, int heightInTiles, float tileSize, int collisionTileFactor) {
		this.tileSize = tileSize;
		this.collisionTileFactor = collisionTileFactor;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.tiles = new TileMap.Tile[widthInTiles][heightInTiles];

		Stopwatch stopwatch = new Stopwatch("init tile map tiles array");
		for (int x = 0; x < widthInTiles; x++) {
			this.tiles[x] = new TileMap.Tile[heightInTiles];
			for (int y = 0; y < heightInTiles; y++) {
				this.tiles[x][y] = new Tile(x, y);
			}
		}
		stopwatch.stop();
		this.mapCollision = new MapCollision();

		this.fixEntityCollisionMap = new ActiveCollisionMap(widthInTiles * collisionTileFactor, heightInTiles * collisionTileFactor);
		this.flexibleEntityCollisionMap = new ActiveCollisionMap(widthInTiles * collisionTileFactor, heightInTiles * collisionTileFactor);
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.iterateVisibleTiles((tile) -> {
			RenderUtil.render(context, tile);
		});
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {

		this.iterateVisibleTiles((tile) -> {
			for (int cx = 0; cx < this.collisionTileFactor; cx++) {
				for (int cy = 0; cy < this.collisionTileFactor; cy++) {
					int x = tile.x * this.collisionTileFactor + cx;
					int y = tile.y * this.collisionTileFactor + cy;
					if (this.mapCollision.isCollision(x, y)) {
						RenderUtil.render(context, new IF_RenderInformation() {
							@Override
							public ComplexRectangle getRenderedRectangle() {
								return new ComplexRectangle(new Position(x * TileMap.this.getCollisionTileSize(), y * TileMap.this.getCollisionTileSize()), new Dimension(TileMap.this
										.getCollisionTileSize(), TileMap.this.getCollisionTileSize()));
							}

							@Override
							public int getRenderableKey() {
								return PredefinedRenderId.RECTANGLE;
							}
						});
					}
				}
			}

		});
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

	private void iterateVisibleTiles(Consumer<Tile> consumer) {
		int startX = (int) Math.floor(RenderConfig.INSTANCE.viewport.getAX() / this.tileSize);
		int startY = (int) Math.floor(RenderConfig.INSTANCE.viewport.getAY() / this.tileSize);
		int endX = startX + (int) Math.ceil(RenderConfig.INSTANCE.viewport.getWidth() / this.tileSize) + 1;
		int endY = startY + (int) Math.ceil(RenderConfig.INSTANCE.viewport.getHeight() / this.tileSize) + 1;
		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;
		if (endX > this.tiles.length)
			endX = this.tiles.length;
		if (endY > this.tiles[0].length)
			endY = this.tiles[0].length;

		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {
				TileMap<CONTEXT>.Tile tile = this.tiles[x][y];
				consumer.accept(tile);
			}
		}
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
		return null;
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
