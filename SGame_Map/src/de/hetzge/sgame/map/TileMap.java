package de.hetzge.sgame.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.render.DynamicRenderableKey;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.IF_RenderableKey;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class TileMap<CONTEXT extends IF_RenderableContext> implements IF_Map, IF_Renderable<IF_RenderableContext>, Serializable {

	private class Tile implements IF_RenderInformation, Serializable {

		private final TileDefinition tileDefinition;
		private final Rectangle renderedRectangle;
		private final DynamicRenderableKey dynamicRenderableKey; // for reuse

		private final int x;
		private final int y;

		public Tile(int x, int y) {
			this.tileDefinition = new TileDefinition();
			this.renderedRectangle = new Rectangle(new Position(x * TileMap.this.tileSize, y * TileMap.this.tileSize), new Dimension(TileMap.this.tileSize, TileMap.this.tileSize));
			this.dynamicRenderableKey = DynamicRenderableKey.DEFAULT_DYNAMIC_RENDERABLE_KEY;
			this.x = x;
			this.y = y;
		}

		public TileDefinition getTileDefinition() {
			return this.tileDefinition;
		}

		@Override
		public Rectangle getRenderedRectangle() {
			return this.renderedRectangle;
		}

		@Override
		public IF_RenderableKey getRenderableKey() {
			this.dynamicRenderableKey.setKey(this.tileDefinition.getKey());
			return this.dynamicRenderableKey;
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
			boolean isMapCollision = TileMap.this.tiles[tileX][tileY].tileDefinition.getCollision().isCollision(x % TileMap.this.collisionTileFactor, y % TileMap.this.collisionTileFactor);
			if (isMapCollision)
				return true;

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

	public TileMap(int widthInTiles, int heightInTiles, float tileSize, int collisionTileFactor) {
		this.tileSize = tileSize;
		this.collisionTileFactor = collisionTileFactor;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.tiles = new TileMap.Tile[widthInTiles][];
		for (int x = 0; x < widthInTiles; x++) {
			this.tiles[x] = new TileMap.Tile[heightInTiles];
			for (int y = 0; y < heightInTiles; y++) {
				this.tiles[x][y] = new Tile(x, y);
			}
		}
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
							public Rectangle getRenderedRectangle() {
								return new Rectangle(new Position(x * TileMap.this.getCollisionTileSize(), y * TileMap.this.getCollisionTileSize()), new Dimension(TileMap.this.getCollisionTileSize(),
										TileMap.this.getCollisionTileSize()));
							}

							@Override
							public IF_RenderableKey getRenderableKey() {
								return IF_RenderableKey.DEFAULT_RECTANGLE_KEY;
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
		int startX = (int) Math.floor(RenderConfig.INSTANCE.viewport.getStartPosition().getX() / this.tileSize);
		int startY = (int) Math.floor(RenderConfig.INSTANCE.viewport.getStartPosition().getY() / this.tileSize);
		int endX = startX + (int) Math.ceil(RenderConfig.INSTANCE.viewport.getDimension().getWidth() / this.tileSize) + 1;
		int endY = startY + (int) Math.ceil(RenderConfig.INSTANCE.viewport.getDimension().getHeight() / this.tileSize) + 1;
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
	public float getWidthInPx() {
		return this.widthInTiles * this.tileSize;
	}

	@Override
	public float getHeightInPx() {
		return this.heightInTiles * this.tileSize;
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
