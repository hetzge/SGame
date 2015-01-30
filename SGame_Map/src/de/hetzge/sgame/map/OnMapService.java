package de.hetzge.sgame.map;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.common.geometry.Position;

public class OnMapService {

	public class On {

		private final IF_ImmutablePrimitivRectangle rectangle;

		private final int startCollisionTileX;
		private final int startCollisionTileY;
		private final int endCollisionTileX;
		private final int endCollisionTileY;

		private final int startTileX;
		private final int startTileY;
		private final int endTileX;
		private final int endTileY;

		private final int widthInCollisionTiles;
		private final int heightInCollisionTiles;
		private final int widthInTiles;
		private final int heightInTiles;

		public On(IF_ImmutablePrimitivRectangle rectangle) {
			IF_Map map = OnMapService.this.mapProvider.provide();

			this.rectangle = rectangle;

			this.startCollisionTileX = map.convertPxInCollisionTile(rectangle.getAX());
			this.startCollisionTileY = map.convertPxInCollisionTile(rectangle.getAY());
			this.endCollisionTileX = map.convertPxInCollisionTile(rectangle.getCX());
			this.endCollisionTileY = map.convertPxInCollisionTile(rectangle.getCY());

			this.startTileX = map.convertPxInTile(rectangle.getAX());
			this.startTileY = map.convertPxInTile(rectangle.getAY());
			this.endTileX = map.convertPxInTile(rectangle.getCX());
			this.endTileY = map.convertPxInTile(rectangle.getCY());

			this.widthInCollisionTiles = this.endCollisionTileX - this.startCollisionTileX + 1;
			this.heightInCollisionTiles = this.endCollisionTileY - this.startCollisionTileY + 1;

			this.widthInTiles = this.endTileX - this.startTileX + 1;
			this.heightInTiles = this.endTileY - this.startTileY + 1;
		}

		public boolean[][] asCollisionArray() {
			boolean[][] collisionArray = new boolean[this.widthInCollisionTiles][this.heightInCollisionTiles];
			for (int x = 0; x < this.widthInCollisionTiles; x++) {
				for (int y = 0; y < this.heightInCollisionTiles; y++) {
					collisionArray[x][y] = true;
				}
			}
			return collisionArray;
		}

		public IF_ImmutablePosition<?> findCollisionPositionAround(On other) {

		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundTop(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundBottom(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.endCollisionTileY + 1;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundLeft(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = startY + other.heightInCollisionTiles;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundRight(On other) {
			int startX = this.endCollisionTileX + 1;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = this.endCollisionTileY + 1;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundRow(int startX, int startY, int endX, int endY, int width, int height) {
			IF_Map map = OnMapService.this.mapProvider.provide();

			for (int x = startX; x < endX; x += width) {
				outer: for (int y = startY; y < endY; y += height) {
					for (int ix = 0; ix < width; ix++) {
						for (int iy = 0; iy < height; iy++) {
							boolean collision = OnMapService.this.mapProvider.provide().getFixEntityCollisionMap().isCollision(x + ix, y + iy);
							if (collision) {
								continue outer;
							}
						}
					}
					return new Position(map.convertCollisionTileInPx(startX) + map.convertCollisionTileInPx(width) / 2, map.convertCollisionTileInPx(startY) + map.convertCollisionTileInPx(height) / 2);
				}
			}
			return null;
		}
	}

	private final IF_MapProvider mapProvider;

	public OnMapService(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

}
