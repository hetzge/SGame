package de.hetzge.sgame.map;

import java.io.Serializable;

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

		public Tile(int x, int y) {
			this.tileDefinition = new TileDefinition();
			this.renderedRectangle = new Rectangle(new Position(x * TileMap.this.tileSize, y * TileMap.this.tileSize), new Dimension(TileMap.this.tileSize, TileMap.this.tileSize));
			this.dynamicRenderableKey = DynamicRenderableKey.DEFAULT_DYNAMIC_RENDERABLE_KEY;
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

	private class MapCollision implements IF_Collision {

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
			int collisionTileFactor = MapConfig.INSTANCE.collisionTileFacor;
			int tileX = x - (x % collisionTileFactor) / collisionTileFactor;
			int tileY = y - (y % collisionTileFactor) / collisionTileFactor;

			return TileMap.this.tiles[tileX][tileY].tileDefinition.getCollision().isCollision(x % collisionTileFactor, y % collisionTileFactor);
		}

		@Override
		public boolean setCollision(int x, int y, boolean collision) {
			throw new IllegalAccessError("Don't set collision direct on map");
		}

	}

	private final Tile[][] tiles;
	private final int tileSize;
	private final int widthInTiles;
	private final int heightInTiles;
	private final MapCollision mapCollision;

	public TileMap(int widthInTiles, int heightInTiles, int tileSize) {
		this.tileSize = tileSize;
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
	}

	@Override
	public void render(IF_RenderableContext context) {

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
				RenderUtil.render(context, tile);
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
	public float getWidthInTiles() {
		return this.widthInTiles;
	}

	@Override
	public float getHeightInTiles() {
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
}
