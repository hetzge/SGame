package de.hetzge.sgame.map;

import java.util.function.Supplier;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.map.message.TileMapMessage;
import de.hetzge.sgame.map.message.TileMapMessageHandler;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.IF_RenderableLoader;

public class MapModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	protected final MapConfig mapConfig;
	protected final TilePool tilePool;
	protected final MessageHandlerPool messageHandlerPool;
	protected final MessageConfig messageConfig;
	protected final IF_RenderableLoader renderableLoader;
	protected final IF_MapProvider mapProvider;
	protected final MapRenderer mapRenderer;

	public MapModule(MapConfig mapConfig, TilePool tilePool, MessageHandlerPool messageHandlerPool, MessageConfig messageConfig, IF_RenderableLoader renderableLoader, IF_MapProvider mapProvider, MapRenderer mapRenderer) {
		this.mapConfig = mapConfig;
		this.tilePool = tilePool;
		this.messageHandlerPool = messageHandlerPool;
		this.messageConfig = messageConfig;
		this.renderableLoader = renderableLoader;
		this.mapProvider = mapProvider;
		this.mapRenderer = mapRenderer;
		TileMap tileMap = new TileMap(mapConfig);
		mapProvider.setMap(tileMap);
		tileMap.initTIleRenderIds(renderableLoader);
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(TileMapMessage.class, this.get(TileMapMessageHandler.class));
		this.messageConfig.serverToNewClientMessages.add((Supplier<?>) () -> {
			return new TileMapMessage((TileMap) this.mapProvider.provide());
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
		this.mapRenderer.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		this.mapRenderer.renderShapes(context);
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		this.mapRenderer.renderFilledShapes(context);
	}

}
