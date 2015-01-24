package de.hetzge.sgame.map;

import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.map.message.TileMapMessage;
import de.hetzge.sgame.map.message.TileMapMessageHandler;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;

public class MapModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	protected TileMap<?> tileMap;

	protected final MapConfig mapConfig;
	protected final TilePool tilePool;

	public MapModule(MapConfig mapConfig, TilePool tilePool) {
		this.mapConfig = mapConfig;
		this.tilePool = tilePool;
		this.tileMap = new TileMap<>(mapConfig);
	}

	@Override
	public void init() {
		MapContext.INSTANCE.set(this);

		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(TileMapMessage.class, this.get(TileMapMessageHandler.class));
		MessageConfig.INSTANCE.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new TileMapMessage(this.tileMap);
		});
	}

	@Override
	public void postInit() {
		MapContext.INSTANCE.set(this);
	}

	@Override
	public void update() {
		MapContext.INSTANCE.set(this);
	}

	@Override
	public void render(IF_RenderableContext context) {
		MapContext.INSTANCE.set(this);
		this.tileMap.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		MapContext.INSTANCE.set(this);
		this.tileMap.renderShapes(context);
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		MapContext.INSTANCE.set(this);
		this.tileMap.renderFilledShapes(context);
	}

	public TileMap<?> getTileMap() {
		return this.tileMap;
	}

	public void setTileMap(TileMap<?> tileMap) {
		this.tileMap = tileMap;
	}

}
