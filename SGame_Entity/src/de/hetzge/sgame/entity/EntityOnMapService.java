package de.hetzge.sgame.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.Predicator;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.definition.IF_ReserveMap;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Mutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Rectangle_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

public class EntityOnMapService {

	public class IgnoreEntityCollisionWrapper implements IF_Collision {

		private final IF_Collision collision;

		private final int ignoreCollisionX;
		private final int ignoreCollisionY;

		private final int ignoreCollisionWidth;
		private final int ignoreCollisionHeight;

		public IgnoreEntityCollisionWrapper(IF_Collision collision, Entity ignoreEntity) {
			this.collision = collision;

			IF_Coordinate_Immutable entityCollisionTileStartCoordinate = EntityOnMapService.this.entityCollisionTileStartCoordinate(ignoreEntity);
			this.ignoreCollisionX = entityCollisionTileStartCoordinate.getColumn();
			this.ignoreCollisionY = entityCollisionTileStartCoordinate.getRow();

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

		private final IF_Rectangle_Immutable rectangle;

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

		public On(IF_Rectangle_Immutable rectangle) {
			IF_Map map = EntityOnMapService.this.mapProvider.provide();

			this.rectangle = rectangle;

			IF_Position_Immutable positionA = rectangle.getA();
			IF_Position_Immutable positionD = rectangle.getD();
			IF_Coordinate_Immutable collisionTilePositionA = map.convertPxXYInCollisionTileXY(positionA);
			IF_Coordinate_Immutable tilePositionA = map.convertPxXYInTileXY(positionA);
			IF_Coordinate_Immutable collisionTilePositionD = map.convertPxXYInCollisionTileXY(positionD);
			IF_Coordinate_Immutable tilePositionD = map.convertPxXYInTileXY(positionD);

			this.startCollisionTileX = collisionTilePositionA.getColumn();
			this.startCollisionTileY = collisionTilePositionA.getRow();
			this.endCollisionTileX = collisionTilePositionD.getColumn();
			this.endCollisionTileY = collisionTilePositionD.getRow();

			this.startTileX = tilePositionA.getColumn();
			this.startTileY = tilePositionA.getRow();
			this.endTileX = tilePositionD.getColumn();
			this.endTileY = tilePositionD.getRow();

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
		public IF_Coordinate_Immutable findEmptyCoordinateAround(Predicator<IF_Coordinate_Immutable> predicator) {
			IF_Coordinate_Mutable coordinate = new XY(0, 0);

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
				coordinate.setColumn(xs[i2]);
				coordinate.setRow(ys[i2]);
				boolean isCollision = this.checkCollisionCoordinate(predicator, coordinate);
				if (!isCollision) {
					return coordinate;
				}
			}

			return null;
		}

		private boolean checkCollisionCoordinate(Predicator<IF_Coordinate_Immutable> predicator, IF_Coordinate_Immutable coordinate) {
			int x = coordinate.getColumn();
			int y = coordinate.getRow();

			boolean isOnCollisionMap = EntityOnMapService.this.mapProvider.provide().isOnCollisionMap(x, y);
			if (isOnCollisionMap) {
				boolean isCollision = predicator.any(coordinate);
				return isCollision;
			} else {
				return true;
			}
		}

	}

	public final Predicate<IF_Coordinate_Immutable> CHECK_FIXED_COLLISION = coordinate -> this.mapProvider.provide().getFixEntityCollisionMap().isCollision(coordinate.getColumn(), coordinate.getRow());
	public final Predicate<IF_Coordinate_Immutable> CHECK_FLEXIBLE_COLLISION = coordinate -> this.mapProvider.provide().getFlexibleEntityCollisionMap().isCollision(coordinate.getColumn(), coordinate.getRow());
	public final Predicate<IF_Coordinate_Immutable> CHECK_RESERVERD = coordinate -> this.reserveMap.isReserved(coordinate);

	private final IF_MapProvider mapProvider;
	private final ActiveEntityMap activeEntityMap;
	private final IF_ReserveMap reserveMap;

	public EntityOnMapService(IF_MapProvider mapProvider, ActiveEntityMap activeEntityMap, IF_ReserveMap reserveMap) {
		this.mapProvider = mapProvider;
		this.activeEntityMap = activeEntityMap;
		this.reserveMap = reserveMap;
	}

	public On on(IF_Rectangle_Immutable rectangle) {
		return new On(rectangle);
	}

	public List<Entity> findEntitiesInAreaAround(Entity around, Predicate<Entity> filter, int radius, int max) {
		List<Entity> result = new LinkedList<>();

		IF_Coordinate_Mutable coordinate = this.mapProvider.provide().convertPxXYInCollisionTileXY(around.getRealRectangle().getCenter()).copy();
		Orientation[] orientations = new Orientation[] { Orientation.NORTH, Orientation.EAST, Orientation.SOUTH, Orientation.WEST };

		mainLoop: for (int i = 0; i < radius * 4; i++) {
			Orientation nextOrientation = orientations[i % 4];
			int add = (int) (Math.floor(i / 2) + 1);
			for (int a = 0; a < add; a++) {
				coordinate.add(nextOrientation.orientationFactor);
				Collection<Entity> connectedObjects = this.activeEntityMap.getConnectedObjects(coordinate.getColumn(), coordinate.getRow());
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
	public IF_Coordinate_Immutable entityCollisionTileStartCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInCollisionTileXY(entity.getRealRectangle().getA());
	}

	public IF_Coordinate_Immutable entityCollisionTileCenterCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInCollisionTileXY(entity.getRealRectangle().getCenter());
	}

	public IF_Coordinate_Immutable entityTileCenterCoordinate(Entity entity) {
		return this.mapProvider.provide().convertPxXYInTileXY(entity.getRealRectangle().getCenter());
	}

	public Path getPathTo(Entity entity, IF_Coordinate_Immutable goalCollisionCoordinate) {
		return new Path(this.entityCollisionTileStartCoordinate(entity), goalCollisionCoordinate, Arrays.asList(goalCollisionCoordinate));
	}

	public Entity circleSearch(IF_Coordinate_Immutable source, int radius, Set<Entity> ignore, Predicator<Entity> predicator) {
		List<Entity> result = this.circleSearch(source, radius, ignore, predicator, 1);
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}


	// TODO test this
	public List<Entity> circleSearch(IF_Coordinate_Immutable source, int radius, Set<Entity> ignore, Predicator<Entity> predicator, int maxResult) {
		int counter = 0;
		int counterGoal = 2 * radius + 1;
		int currentRadius = 0;
		IF_Coordinate_Mutable currentCoordinate = source.copy();
		Orientation orientation = Orientation.NORTH;

		List<Entity> entities = new LinkedList<>();

		List<Entity> foundEntities = this.checkCoordinate(ignore, predicator, currentCoordinate, maxResult - entities.size());
		if (foundEntities != null) {
			entities.addAll(foundEntities);
			if (foundEntities.size() >= maxResult) {
				return entities;
			}
		}

		while (true) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < currentRadius; j++) {
					currentCoordinate.add(orientation.orientationFactor);
					orientation = orientation.nextSimple();
					counter++;

					foundEntities = this.checkCoordinate(ignore, predicator, currentCoordinate, maxResult - entities.size());
					if (foundEntities != null) {
						entities.addAll(foundEntities);
						if (foundEntities.size() >= maxResult) {
							return entities;
						}
					}

					if (counter >= counterGoal) {
						return null;
					}
				}
			}
			radius++;
		}
	}

	private List<Entity> checkCoordinate(Set<Entity> ignore, Predicator<Entity> predicator, IF_Coordinate_Immutable currentCoordinate, int maxResult) {
		List<Entity> result = null;
		Collection<Entity> entities = this.activeEntityMap.getConnectedObjects(currentCoordinate.getColumn(), currentCoordinate.getRow());
		for (Entity entity : entities) {
			boolean match = predicator.all(entity);
			if (match && ignore != null && !ignore.contains(entity)) {
				if (result == null) {
					result = new LinkedList<>();
				}
				result.add(entity);
				if (result.size() >= maxResult) {
					return result;
				}
			}
		}
		return result;
	}

}
