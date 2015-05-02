package de.hetzge.sgame.map.message;

import de.hetzge.sgame.map.TileMap;
import de.hetzge.sgame.message.BaseMessage;

public class TileMapMessage extends BaseMessage {

	public final TileMap tileMap;

	public TileMapMessage(TileMap tileMap) {
		this.tileMap = tileMap;
	}

}
