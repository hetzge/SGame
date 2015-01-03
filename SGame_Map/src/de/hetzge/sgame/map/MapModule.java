package de.hetzge.sgame.map;

import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.map.message.TileMapMessage;
import de.hetzge.sgame.map.message.TileMapMessageHandler;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;

public class MapModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	public MapModule() {
		MapConfig.INSTANCE.setTileMap(new TileMap<IF_RenderableContext>(100, 100, 32f, 3));
	}

	@Override
	public void init() {
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(TileMapMessage.class, new TileMapMessageHandler());

		MessageConfig.INSTANCE.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new TileMapMessage(MapConfig.INSTANCE.getTileMap());
		});
	}

	@Override
	public void update() {
	}

	@Override
	public void render(IF_RenderableContext context) {
		MapConfig.INSTANCE.getTileMap().render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		MapConfig.INSTANCE.getTileMap().renderShapes(context);
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		MapConfig.INSTANCE.getTileMap().renderFilledShapes(context);
	}

}
