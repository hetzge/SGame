package de.hetzge.sgame.common.definition;

public interface IF_Map {

	public float getWidthInPx();

	public float getHeightInPx();

	public int getWidthInTiles();

	public int getHeightInTiles();

	public int getCollisionTileFactor();

	public IF_Collision getCollision();

}
