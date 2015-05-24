package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

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

	public default IF_Position_Immutable convertCollisionTileXYInPxXY(IF_Coordinate_Immutable coordinate) {
		return coordinate.copy().multiply(new XY(this.getCollisionTileSize()));
	}

	public default IF_Position_Immutable convertTileXYInPxXY(IF_Coordinate_Immutable coordinate) {
		return coordinate.copy().multiply(new XY(this.getTileSize()));
	}

	public default IF_Coordinate_Immutable convertPxXYInCollisionTileXY(IF_Position_Immutable position) {
		return position.copy().divide(new XY(this.getCollisionTileSize()));
	}

	public default IF_Coordinate_Immutable convertPxXYInTileXY(IF_Position_Immutable position){
		return position.copy().divide(new XY(this.getTileSize()));
	}

	public default boolean isFixOrFlexibleCollision(int x, int y){
		return this.getFixEntityCollisionMap().isCollision(x, y) || this.getFlexibleEntityCollisionMap().isCollision(x, y);
	}

	public default boolean isOnCollisionMap(int x, int y){
		return x >= 0 && y >= 0 && x < this.getWidthInCollisionTiles() && y < this.getHeightInCollisionTiles();
	}

}
