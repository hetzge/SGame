package de.hetzge.sgame.map.message;

import de.hetzge.sgame.map.MapModule;
import de.hetzge.sgame.message.IF_MessageHandler;

public class TileMapMessageHandler implements IF_MessageHandler<TileMapMessage> {

	private final MapModule mapModule;

	public TileMapMessageHandler(MapModule mapModule) {
		this.mapModule = mapModule;
	}

	@Override
	public void handle(TileMapMessage message) {
		this.mapModule.setTileMap(message.tileMap);
		message.tileMap.init();
	}
}
