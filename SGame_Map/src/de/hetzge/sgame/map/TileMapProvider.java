package de.hetzge.sgame.map;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Map;

public class TileMapProvider implements IF_MapProvider {

	private IF_Map map;

	@Override
	public IF_Map provide() {
		return this.map;
	}

	@Override
	public void setMap(IF_Map map) {
		this.map = map;
	}

}
