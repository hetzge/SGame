package de.hetzge.sgame.common;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;

public class DummyMap implements IF_Map {

	private ActiveCollisionMap collisionMap = new ActiveCollisionMap(100 * 3, 100 * 3);

	@Override
	public int getWidthInTiles() {
		return 100;
	}

	@Override
	public float getTileSize() {
		return 32;
	}

	@Override
	public int getHeightInTiles() {
		return 100;
	}

	@Override
	public int getCollisionTileFactor() {
		return 3;
	}

	@Override
	public IF_Collision getCollision() {
		return this.collisionMap;
	}

	@Override
	public ActiveCollisionMap getFixEntityCollisionMap() {
		return this.collisionMap;
	}

	@Override
	public ActiveCollisionMap getFlexibleEntityCollisionMap() {
		return this.collisionMap;
	}
}