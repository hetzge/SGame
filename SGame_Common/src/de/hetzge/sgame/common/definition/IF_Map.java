package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;

public interface IF_Map {

	public float getTileSize();

	public int getWidthInTiles();

	public int getHeightInTiles();

	public int getCollisionTileFactor();

	public IF_Collision getCollision();

	public ActiveCollisionMap getFixEntityCollisionMap();

	public ActiveCollisionMap getFlexibleEntityCollisionMap();

	public default float getWidthInPx() {
		return this.getWidthInTiles() * this.getTileSize();
	}

	public default float getHeightInPx() {
		return this.getHeightInTiles() * this.getTileSize();
	}

	public default int getWidthInCollisionTiles() {
		return this.getWidthInTiles() * this.getCollisionTileFactor();
	}

	public default int getHeightInCollisionTiles() {
		return this.getHeightInTiles() * this.getCollisionTileFactor();
	}

	public default float getCollisionTileSize() {
		return this.getTileSize() / this.getCollisionTileFactor();
	}

	public default int convertPxInTile(float pixel) {
		return (int) Math.floor(pixel / this.getTileSize());
	}

	public default int convertPxInCollisionTile(float pixel) {
		return (int) Math.floor(pixel / this.getCollisionTileSize());
	}

	public default float convertTileInPx(int tile) {
		return tile * this.getTileSize();
	}

	public default float convertCollisionTileInPx(int collisionTile) {
		return collisionTile * this.getCollisionTileSize();
	}
}
