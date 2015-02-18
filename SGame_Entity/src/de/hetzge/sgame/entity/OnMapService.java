package de.hetzge.sgame.entity;

import java.util.List;
import java.util.function.Predicate;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

public class OnMapService {

	public class On {

		private final IF_Rectangle_ImmutableView rectangle;

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

		public On(IF_Rectangle_ImmutableView rectangle) {
			IF_Map map = OnMapService.this.mapProvider.provide();

			this.rectangle = rectangle;

			IF_Position_ImmutableView positionA = rectangle.getPositionA();
			IF_Position_ImmutableView positionD = rectangle.getPositionD();
			IF_Coordinate collisionTilePositionA = map.convertPxXYInCollisionTileXY(positionA);
			IF_Coordinate tilePositionA = map.convertPxXYInTileXY(positionA);
			IF_Coordinate collisionTilePositionD = map.convertPxXYInCollisionTileXY(positionD);
			IF_Coordinate tilePositionD = map.convertPxXYInTileXY(positionD);

			this.startCollisionTileX = collisionTilePositionA.getIX();
			this.startCollisionTileY = collisionTilePositionA.getIY();
			this.endCollisionTileX = collisionTilePositionD.getIX();
			this.endCollisionTileY = collisionTilePositionD.getIY();

			this.startTileX = tilePositionA.getIX();
			this.startTileY = tilePositionA.getIY();
			this.endTileX = tilePositionD.getIX();
			this.endTileY = tilePositionD.getIY();

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

		public IF_Position_ImmutableView findCollisionPositionAround(On other) {
			Orientation orientation = other.rectangle.getCenteredPosition().orientationToOther(this.rectangle.getCenteredPosition());
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

		private IF_Position_ImmutableView findCollisionPositionAround(On other, Orientation... orientations) {
			for (Orientation orientation : orientations) {
				IF_Position_ImmutableView result = null;
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

		private IF_Position_ImmutableView findCollisionPositionAroundTop(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_Position_ImmutableView findCollisionPositionAroundBottom(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.endCollisionTileY + 1;
			int endX = this.endCollisionTileX + other.widthInCollisionTiles;
			int endY = startY;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_Position_ImmutableView findCollisionPositionAroundLeft(On other) {
			int startX = this.startCollisionTileX - other.widthInCollisionTiles;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = this.endCollisionTileY + 1;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_Position_ImmutableView findCollisionPositionAroundRight(On other) {
			int startX = this.endCollisionTileX + 1;
			int startY = this.startCollisionTileY - other.heightInCollisionTiles;
			int endX = startX;
			int endY = this.endCollisionTileY + 1;
			return this.findCollisionPositionAroundRow(startX, startY, endX, endY, other.widthInCollisionTiles, other.heightInCollisionTiles, other);
		}

		private IF_Position_ImmutableView findCollisionPositionAroundRow(int startX, int startY, int endX, int endY, int width, int height, On other) {
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
					return new XY(map.convertCollisionTileInPx(startX) + map.convertCollisionTileInPx(width) / 2, map.convertCollisionTileInPx(startY) + map.convertCollisionTileInPx(height) / 2);
				}
			}
			return null;
		}
	}

	private final IF_MapProvider mapProvider;

	public OnMapService(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	public On on(IF_Rectangle_ImmutableView rectangle) {
		return new On(rectangle);
	}

	public IF_Position_ImmutableView findPositionAround(Entity around, Entity entity) {
		if (around.positionAndDimensionModuleCache.isAvailable() && entity.positionAndDimensionModuleCache.isAvailable()) {
			IF_Rectangle_ImmutableView aroundRectangle = around.positionAndDimensionModuleCache.get();
			IF_Rectangle_ImmutableView entityRectangle = entity.positionAndDimensionModuleCache.get();
			return this.on(aroundRectangle).findCollisionPositionAround(this.on(entityRectangle));
		} else {
			throw new IllegalArgumentException("The given entities (or one of them) has no position to evaluate position arround.");
		}
	}

	public List<Entity> findEntitiesAround(Predicate<Entity> filter, int radius, int max){


		return null;
	}

}
