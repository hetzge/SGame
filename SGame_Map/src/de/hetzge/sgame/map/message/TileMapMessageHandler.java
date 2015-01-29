package de.hetzge.sgame.map.message;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.message.IF_MessageHandler;
import de.hetzge.sgame.render.IF_RenderableLoader;

public class TileMapMessageHandler implements IF_MessageHandler<TileMapMessage> {

	private final IF_MapProvider mapProvider;
	private final IF_RenderableLoader renderableLoader;

	public TileMapMessageHandler(IF_MapProvider mapProvider, IF_RenderableLoader renderableLoader) {
		this.mapProvider = mapProvider;
		this.renderableLoader = renderableLoader;
	}

	@Override
	public void handle(TileMapMessage message) {
		this.mapProvider.setMap(message.tileMap);
		message.tileMap.initTIleRenderIds(this.renderableLoader);
	}
}
