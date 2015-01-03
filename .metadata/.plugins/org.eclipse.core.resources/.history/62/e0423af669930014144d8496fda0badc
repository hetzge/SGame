package de.hetzge.sgame.map.message;

import de.hetzge.sgame.map.MapConfig;
import de.hetzge.sgame.message.IF_MessageHandler;

public class TileMapMessageHandler implements IF_MessageHandler<TileMapMessage> {

	@Override
	public void handle(TileMapMessage message) {
		MapConfig.INSTANCE.tileMap = message.tileMap;
	}
}
