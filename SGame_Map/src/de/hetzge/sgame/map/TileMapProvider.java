package de.hetzge.sgame.map;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Map;

public class TileMapProvider implements IF_MapProvider {

	private final MapModule mapModule;

	public TileMapProvider(MapModule mapModule) {
		this.mapModule = mapModule;
	}

	@Override
	public IF_Map provide() {
		return this.mapModule.getTileMap();
	}

}
