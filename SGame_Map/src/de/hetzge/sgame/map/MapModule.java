package de.hetzge.sgame.map;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.common.geometry.PrimitivRectangle;
import de.hetzge.sgame.map.message.TileMapMessage;
import de.hetzge.sgame.map.message.TileMapMessageHandler;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.IF_RenderableLoader;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.Viewport;

public class MapModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	protected final MapConfig mapConfig;
	protected final TilePool tilePool;
	protected final MessageHandlerPool messageHandlerPool;
	protected final MessageConfig messageConfig;
	protected final Viewport mapViewport;
	protected final RenderService renderService;
	protected final IF_RenderableLoader renderableLoader;
	protected final IF_MapProvider mapProvider;

	public MapModule(MapConfig mapConfig, TilePool tilePool, MessageHandlerPool messageHandlerPool, MessageConfig messageConfig, Viewport mapViewport, RenderService renderService, IF_RenderableLoader renderableLoader, IF_MapProvider mapProvider) {
		this.mapConfig = mapConfig;
		this.tilePool = tilePool;
		this.messageHandlerPool = messageHandlerPool;
		this.messageConfig = messageConfig;
		this.mapViewport = mapViewport;
		this.renderService = renderService;
		this.renderableLoader = renderableLoader;
		this.mapProvider = mapProvider;
		TileMap tileMap = new TileMap(mapConfig);
		mapProvider.setMap(tileMap);
		tileMap.initTIleRenderIds(renderableLoader);
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(TileMapMessage.class, this.get(TileMapMessageHandler.class));
		this.messageConfig.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new TileMapMessage(this.getTileMap());
		});
	}

	@Override
	public void postInit() {
	}

	@Override
	public void update() {
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
		this.mapViewport.iterateVisibleTiles((tileX, tileY) -> {
			for (int cx = 0; cx < this.getTileMap().getCollisionTileFactor(); cx++) {
				for (int cy = 0; cy < this.getTileMap().getCollisionTileFactor(); cy++) {
					if (this.getTileMap().getCollision().isCollision(tileX * this.getTileMap().getCollisionTileFactor() + cx, tileY * this.getTileMap().getCollisionTileFactor() + cy)) {
						this.renderService.render(context, new PrimitivRectangle(tileX * this.getTileMap().getTileSize() + cx * this.getTileMap().getCollisionTileSize(), tileY * this.getTileMap().getTileSize() + cy * this.getTileMap().getCollisionTileSize(), this.getTileMap().getCollisionTileSize(), this.getTileMap().getCollisionTileSize()), PredefinedRenderId.RECTANGLE);
					}
				}
			}
			return null;
		});
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

	public TileMap getTileMap() {
		return (TileMap) this.mapProvider.provide();
	}

}
