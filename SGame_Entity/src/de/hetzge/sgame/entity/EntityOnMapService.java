package de.hetzge.sgame.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.definition.IF_ReserveMap;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

public class EntityOnMapService {

	public class IgnoreEntityCollisionWrapper implements IF_Collision {

		private final IF_Collision collision;

		private final int ignoreCollisionX;
		private final int ignoreCollisionY;

		private final int ignoreCollisionWidth;
		private final int ignoreCollisionHeight;

		public IgnoreEntityCollisionWrapper(IF_Collision collision, Entity ignoreEntity) {
			this.collision = collision;

			IF_Coordinate_ImmutableView entityCollisionTileStartCoordinate = EntityOnMapService.this.entityCollisionTileStartCoordinate(ignoreEntity);
			this.ignoreCollisionX = entityCollisionTileStartCoordinate.getIX();
			this.ignoreCollisionY = entityCollisionTileStartCoordinate.getIY();

			this.ignoreCollisionWidth = ignoreEntity.getActiveCollisionMap().getWidthInTiles();
			this.ignoreCollisionHeight = ignoreEntity.getActiveCollisionMap().getHeightInTiles();
		}

		@Override
		public int getWidthInTiles() {
			return this.collision.getWidthInTiles();
		}

		@Override
		public int getHeightInTiles() {
			return this.collision.getHeightInTiles();
		}

		@Override
		public boolean isCollision(int x, int y) {
			return this.collision.isCollision(x, y) && !(x >= this.ignoreCollisionX && y >= this.ignoreCollisionY && x < this.ignoreCollisionX + this.ignoreCollisionWidth && y < this.ignoreCollisionY + this.ignoreCollisionHeight);
		}

		@Override
		public void setCollision(int x, int y, boolean collision) {
			this.collision.setCollision(x, y, collision);
		}

	}

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
			IF_Map map = EntityOnMapService.this.mapProvider.provide();

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

		/**
		 * Checks if around a entity is a non colliding coordinate. Return null
		 * if no coordinate is found.
		 */
		public IF_Coordinate_ImmutableView findEmptyCoordinateAround(Predicate<IF_Coordinate_ImmutableView>... predicates) {
			List<Predicate<IF_Coordinate_ImmutableView>> predicatesList = Arrays.asList(predicates);
			IF_Coordinate_MutableView coordinate = new XY(0, 0);

			int positionToCheckCount = (this.widthInCollisionTiles + 2) * 2 + this.heightInCollisionTiles * 2;
			int[] xs = new int[positionToCheckCount];
			int[] ys = new int[positionToCheckCount];

			int i = 0;
			for (int x = this.startCollisionTileX - 1, y = this.startCollisionTileY - 1; x <= this.endCollisionTileX + 1; x++, i++) {
				xs[i] = x;
				ys[i] = y;
			}
			for (int x = this.startCollisionTileX - 1, y = this.endCollisionTileY + 1; x <= this.endCollisionTileX + 1; x++, i++) {
				xs[i] = x;
				ys[i] = y;
			}
			for (int x = this.startCollisionTileX - 1, y = this.startCollisionTileY; y <= this.endCollisionTileY; y++, i++) {
				xs[i] = x;
				ys[i] = y;
			}
			for (int x = this.endCollisionTileX + 1, y = this.startCollisionTileY; y <= this.endCollisionTileY; y++, i++) {
				xs[i] = x;
				ys[i] = y;
			}

			for (int i2 = 0; i2 < xs.length; i2++) {
				coordinate.setIX(xs[i2]);
				coordinate.setIY(ys[i2]);
				boolean isCollision = this.checkCollisionCoordinate(predicatesList, coordinate);
				if (!isCollision) {
					return coordinate;
				}
			}

			return null;
		}

		private boolean checkCollisionCoordinate(List<Predicate<IF_Coordinate_ImmutableView>> predicatesList, IF_Coordinate_ImmutableView coordinate) {
			int x = coordinate.getIX();
			int y = coordinate.getIY();

			boolean isOnCollisionMap = EntityOnMapService.this.mapProvider.provide().isOnCollisionMap(x, y);
			if (isOnCollisionMap) {
				boolean isCollision = this.anyPredicateMatch(predicatesList, coordinate);
				return isCollision;
			} else {
				return true;
			}
		}

		private boolean anyPredicateMatch(List<Predicate<IF_Coordinate_ImmutableView>> predicatesList, IF_Coordinate_ImmutableView coordinate) {
			return predicatesList.stream().anyMatch(predicate -> predicate.test(coordinate));
		}

	}

	public final Predicate<IF_Coordinate_ImmutableView> CHECK_FIXED_COLLISION = coordinate -> this.mapProvider.provide().getFixEntityCollisionMap().isCollision(coordinate.getIX(), coordinate.getIY());
	public final Predicate<IF_Coordinate_ImmutableView> CHECK_FLEXIBLE_COLLISION = coordinate -> this.mapProvider.provide().getFlexibleEntityCollisionMap().isCollision(coordinate.getIX(), coordinate.getIY());
	public final Predicate<IF_Coordinate_ImmutableView> CHECK_RESERVERD = coordinate -> this.reserveMap.isReserved(coordinate);

	private final IF_MapProvider mapProvider;
	private final ActiveEntityMap activeEntityMap;
	private final IF_ReserveMap reserveMap;

	public EntityOnMapService(IF_MapProvider mapProvider, ActiveEntityMap activeEntityMap, IF_ReserveMap reserveMap) {
		this.mapProvider = mapProvider;
		this.activeEntityMap = activeEntityMap;
		this.reserveMap = reserveMap;
	}

	public On on(IF_Rectangle_ImmutableView rectangle) {
		return new On(rectangle);
	}

	public List<Entity> findEntitiesInAreaAround(Entity around, Predicate<Entity> filter, int radius, int max) {
		List<Entity> result = new LinkedList<>();

		IF_Coordinate coordinate = this.mapProvider.provide().convertPxXYInCollisionTileXY(around.getRealRectangle().getCenteredPosition());
		Orientation[] orientations = new Orientation[] { Orientation.NORTH, Orientation.EAST, Orientation.SOUTH, Orientation.WEST };

		mainLoop: for (int i = 0; i < radius * 4; i++) {
			Orientation nextOrientation = orientations[i % 4];
			int add = (int) (Math.floor(i / 2) + 1);
			for (int a = 0; a < add; a++) {
				coordinate.add(nextOrientation.orientationFactor);
				Collection<Entity> connectedObjects = this.activeEntityMap.getConnectedObjects(coordinate.getIX(), coordinate.getIY());
				for (Entity entity : connectedObjects) {
					if (filter.test(entity) && !entity.equals(around)) {
						result.add(entity);
						if (result.size() >= max) {
							break mainLoop;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * The top left corner of the entity rectangle as collision coordinate.
	 */
	public IF_Coordinate_ImmutableView entityCollisionTileStartCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInCollisionTileXY(entity.getRealRectangle().getPositionA().asPositionImmutableView());
	}

	public IF_Coordinate_ImmutableView entityCollisionTileCenterCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInCollisionTileXY(entity.getRealRectangle().getCenteredPosition());
	}

	public IF_Coordinate_ImmutableView entityTileCenterCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInTileXY(entity.getRealRectangle().getCenteredPosition());
	}

	public Path getPathTo(Entity entity, IF_Coordinate_ImmutableView goalCollisionCoordinate) {
		return new Path(this.entityCollisionTileStartCoordinate(entity), goalCollisionCoordinate, Arrays.asList(goalCollisionCoordinate), this.mapProvider.provide());
	}
}
