package de.hetzge.sgame.common;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;

public class DummyMap implements IF_Map {
	@Override
	public int getWidthInTiles() {
		return 500;
	}

	@Override
	public float getTileSize() {
		return 32;
	}

	@Override
	public int getHeightInTiles() {
		return 500;
	}

	@Override
	public int getCollisionTileFactor() {
		return 3;
	}

	@Override
	public IF_Collision getCollision() {
		return null;
	}

	@Override
	public ActiveCollisionMap getFixEntityCollisionMap() {
		return null;
	}

	@Override
	public ActiveCollisionMap getFlexibleEntityCollisionMap() {
		return null;
	}
}