package de.hetzge.sgame.map.message;

import java.io.Serializable;

import de.hetzge.sgame.map.TileMap;

public class TileMapMessage implements Serializable {

	public final TileMap tileMap;

	public TileMapMessage(TileMap tileMap) {
		this.tileMap = tileMap;
	}

}
