package de.hetzge.sgame.common;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;

public class DummyMap implements IF_Map {
	
	private ActiveCollisionMap collisionMap = new ActiveCollisionMap(500 * 3, 500 * 3);
	
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
		return collisionMap;
	}

	@Override
	public ActiveCollisionMap getFixEntityCollisionMap() {
		return collisionMap;
	}

	@Override
	public ActiveCollisionMap getFlexibleEntityCollisionMap() {
		return collisionMap;
	}
}