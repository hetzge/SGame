package de.hetzge.sgame.entity;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
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
			this.endCollisionTileX = map.convertPxInCollisionTile(rectangle.getDX());
			this.endCollisionTileY = map.convertPxInCollisionTile(rectangle.getDY());

			this.startTileX = map.convertPxInTile(rectangle.getAX());
			this.startTileY = map.convertPxInTile(rectangle.getAY());
			this.endTileX = map.convertPxInTile(rectangle.getDX());
			this.endTileY = map.convertPxInTile(rectangle.getDY());

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
			Orientation orientation = new Position(other.rectangle.getX(), other.rectangle.getY()).evaluateOrientationToOtherPosition(new Position(this.rectangle.getX(), this.rectangle.getY()));
			switch (orientation) {
			case EAST:
				return this.findCollisionPositionAround(other, Orientation.EAST, Orientation.NORTH, Orientation.SOUTH, Orientation.WEST);
			case NORTH:
				return this.findCollisionPositionAround(other, Orientation.NORTH, Orientation.EAST, Orientation.WEST, Orientation.SOUTH);
			case SOUTH:
				return this.findCollisionPositionAround(other, Orientation.SOUTH, Orientation.EAST, Orientation.SOUTH, Orientation.WEST, Orientation.NORTH);
			case WEST:
				return this.findCollisionPositionAround(other, Orientation.WEST, Orientation.NORTH, Orientation.SOUTH, Orientation.EAST);
			default:
				throw new IllegalStateException();
			}
		}

		private IF_ImmutablePosition<?> findCollisionPositionAround(On other, Orientation... orientations) {
			for (Orientation orientation : orientations) {
				IF_ImmutablePosition<?> result = null;
				switch (orientation) {
				case EAST:
					result = this.findCollisionPositionAroundRight(other);
					break;
				case NORTH:
					result = this.findCollisionPositionAroundTop(other);
					break;
				case SOUTH:
					result = this.findCollisionPositionAroundBottom(other);
					break;
				case WEST:
					result = this.findCollisionPositionAroundLeft(other);
					break;
				default:
					throw new IllegalStateException();
				}
				if (result != null) {
					return result;
				}
			}
			return null;
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundTop(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundBottom(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.endCollisionTileY + 1;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundLeft(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = startY + other.heightInCollisionTiles;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundRight(On other) {
			int startX = this.endCollisionTileX + 1;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = this.endCollisionTileY + 1;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_ImmutablePosition<?> findCollisionPositionAroundRow(int startX, int startY, int endX, int endY, int width, int height, On other) {
			IF_Map map = OnMapService.this.mapProvider.provide();

			Integer[] xValues = Util.valuesInsideOut(startX, endX);
			Integer[] yValues = Util.valuesInsideOut(startY, endY);

			for (Integer x : xValues) {
				outer: for (Integer y : yValues) {
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

	public On on(IF_ImmutablePrimitivRectangle rectangle) {
		return new On(rectangle);
	}

	public IF_ImmutablePosition<?> findPositionAround(Entity around, Entity entity) {
		if (around.positionAndDimensionModuleCache.isAvailable() && entity.positionAndDimensionModuleCache.isAvailable()) {
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> aroundRectangle = around.positionAndDimensionModuleCache.get().getPositionAndDimensionRectangle();
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> entityRectangle = entity.positionAndDimensionModuleCache.get().getPositionAndDimensionRectangle();
			return this.on(aroundRectangle).findCollisionPositionAround(this.on(entityRectangle));
		} else {
			throw new IllegalArgumentException("The given entities (or one of them) has no position to evaluate position arround.");
		}
	}

}
