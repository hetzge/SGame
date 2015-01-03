package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.activemap.ActiveMap;

public interface IF_Map {

	public float getWidthInPx();

	public float getHeightInPx();

	public float getTileSize();

	public float getCollisionTileSize();

	public int getWidthInTiles();

	public int getHeightInTiles();

	public int getCollisionTileFactor();

	public IF_Collision getCollision();

	public ActiveMap<Boolean> getEntityCollisionActiveMap();

}
