package de.hetzge.sgame.map;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.Viewport;

public class MapRenderer implements IF_Renderable<IF_RenderableContext> {

	protected final Viewport mapViewport;
	protected final RenderService renderService;
	private final IF_MapProvider mapProvider;

	public MapRenderer(Viewport mapViewport, RenderService renderService, IF_MapProvider mapProvider) {
		this.mapViewport = mapViewport;
		this.renderService = renderService;
		this.mapProvider = mapProvider;
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.mapViewport.iterateVisibleTiles((x, y) -> {
			TileMap.Tile tile = this.getTileMap().getTile(x, y);
			this.renderService.render(context, tile, tile.getRenderId());
			return null;
		});
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		//		this.mapViewport.iterateVisibleTiles((tileX, tileY) -> {
		//			for (int cx = 0; cx < this.getTileMap().getCollisionTileFactor(); cx++) {
		//				for (int cy = 0; cy < this.getTileMap().getCollisionTileFactor(); cy++) {
		//					if (this.getTileMap().getFixEntityCollisionMap().isCollision(tileX * this.getTileMap().getCollisionTileFactor() + cx, tileY * this.getTileMap().getCollisionTileFactor() + cy)) {
		//
		//						Rectangle collisionTileRectangle = new Rectangle();
		//						collisionTileRectangle.setDimension(new XY(this.getTileMap().getCollisionTileSize(), this.getTileMap().getCollisionTileSize()));
		//						collisionTileRectangle.setPositionA(new XY(tileX * this.getTileMap().getTileSize() + cx * this.getTileMap().getCollisionTileSize(), tileY * this.getTileMap().getTileSize() + cy * this.getTileMap().getCollisionTileSize()));
		//
		//						this.renderService.render(context, collisionTileRectangle, PredefinedRenderId.RECTANGLE);
		//					}
		//
		//					if (this.getTileMap().getFlexibleEntityCollisionMap().isCollision(tileX * this.getTileMap().getCollisionTileFactor() + cx, tileY * this.getTileMap().getCollisionTileFactor() + cy)) {
		//
		//						Rectangle collisionTileRectangle = new Rectangle();
		//						collisionTileRectangle.setDimension(new XY(this.getTileMap().getCollisionTileSize(), this.getTileMap().getCollisionTileSize()));
		//						collisionTileRectangle.setPositionA(new XY(tileX * this.getTileMap().getTileSize() + cx * this.getTileMap().getCollisionTileSize(), tileY * this.getTileMap().getTileSize() + cy * this.getTileMap().getCollisionTileSize()));
		//
		//						this.renderService.render(context, collisionTileRectangle, PredefinedRenderId.RECTANGLE);
		//					}
		//				}
		//			}
		//			return null;
		//		});
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

	private TileMap getTileMap() {
		return (TileMap) this.mapProvider.provide();
	}

}
