package de.hetzge.sgame.entity.ki.low;

import java.util.Collection;
import java.util.EnumSet;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.entity.ActiveEntityMap;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.EntityOnMapService.IgnoreEntityCollisionWrapper;
import de.hetzge.sgame.entity.ki.BaseKI;

public class GotoEntityKI extends BaseKI {

	private final Entity gotoEntity;

	private final IF_MapProvider mapProvider = this.get(IF_MapProvider.class);
	private final EntityOnMapService entityOnMapService = this.get(EntityOnMapService.class);
	private final AStarService aStarService = this.get(AStarService.class);
	private final PathfinderThread pathfinderThread = this.get(PathfinderThread.class);

	private PathfinderWorker pathfinderWorker;

	public GotoEntityKI(Entity entity, Entity gotoEntity) {
		super(entity);
		this.gotoEntity = gotoEntity;

		Log.KI.debug("Created GotoKI for entity " + entity + " to " + gotoEntity);
	}

	@Override
	protected boolean condition() {
		return this.isFarAwayEnought();
	}

	@Override
	protected KIState initImpl() {
		IF_Map map = this.mapProvider.provide();
		ActiveCollisionMap fixEntityCollisionMap = map.getFixEntityCollisionMap();
		IgnoreEntityCollisionWrapper ignoreEntityCollisionWrapper = this.entityOnMapService.new IgnoreEntityCollisionWrapper(fixEntityCollisionMap, this.gotoEntity);

		IF_Coordinate_ImmutableView entityCollisionTilePosition = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.entity);
		int startX = entityCollisionTilePosition.getIX();
		int startY = entityCollisionTilePosition.getIY();

		IF_Coordinate_ImmutableView goalEntityCollisionTilePosition = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.gotoEntity);
		int goalX = goalEntityCollisionTilePosition.getIX();
		int goalY = goalEntityCollisionTilePosition.getIY();

		this.pathfinderWorker = this.pathfinderThread.new PathfinderWorker() {
			@Override
			public Path findPath() {
				// TODO entity collision
				return GotoEntityKI.this.aStarService.findPath(ignoreEntityCollisionWrapper, startX, startY, goalX, goalY, new boolean[0][0]);
			}
		};

		return KIState.ACTIVE;
	}

	@Override
	protected KIState updateImpl() {

		if (this.pathfinderWorker != null && this.pathfinderWorker.done()) {
			Path path = this.pathfinderWorker.get();
			this.pathfinderWorker = null;
			if (path.isPathNotPossible()) {
				return KIState.FAILURE;
			}
			this.entity.setPath(path);
		}

		if (!this.isFarAwayEnought()) {
			return KIState.SUCCESS;
		}

		this.entity.continueOnPath();
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
		if (!this.canStayHere()) {
			this.changePosition();
		}
		this.entity.unsetPath();
	}

	private void changePosition() {
		IF_Coordinate_ImmutableView coordinate = this.findChangePosition();
		if (coordinate != null) {
			this.changeActiveKI(new GotoKI(this.entity, coordinate));
		}
	}

	/**
	 * Search a position around current position which is not nearer on the
	 * goal. Return null if no position found.
	 */
	private IF_Coordinate_ImmutableView findChangePosition() {
		IF_Map map = this.mapProvider.provide();

		IF_Position_ImmutableView entityPosition = this.entity.getRealRectangle().getCenteredPosition();
		IF_Position_ImmutableView gotoEntityPosition = this.gotoEntity.getRealRectangle().getCenteredPosition();
		IF_Coordinate_ImmutableView entityCollisionTileCenterCoordinate = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.entity);

		Orientation orientationToOther = entityPosition.orientationToOther(gotoEntityPosition);

		EnumSet<Orientation> checkOrientations;
		if (orientationToOther.isVertical()) {
			checkOrientations = EnumSet.copyOf(Orientation.Horizontal);
		} else if (orientationToOther.isHorizontal()) {
			checkOrientations = EnumSet.copyOf(Orientation.Vertical);
		} else {
			throw new IllegalStateException();
		}
		checkOrientations.add(orientationToOther);

		for (Orientation orientation : checkOrientations) {
			IF_Coordinate_ImmutableView coordinate = entityCollisionTileCenterCoordinate.copy().add(orientation.orientationFactor);

			int x = coordinate.getIX();
			int y = coordinate.getIY();
			boolean collision = map.isFixOrFlexibleCollision(x, y);

			if (!collision) {
				return coordinate;
			}
		}

		return null;
	}

	private boolean canStayHere() {
		IF_Coordinate_ImmutableView entityCollisionTileCenterCoordinate = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.entity);

		int x = entityCollisionTileCenterCoordinate.getIX();
		int y = entityCollisionTileCenterCoordinate.getIY();
		Collection<Entity> entitiesOnMapPosition = this.get(ActiveEntityMap.class).getConnectedObjects(x, y);
		boolean otherFlexibleEntityOnMapPosition = entitiesOnMapPosition.stream().anyMatch(this::otherFlexibleEntityPredicate);

		return !otherFlexibleEntityOnMapPosition;
	}

	private boolean otherFlexibleEntityPredicate(Entity entity) {
		return !entity.isFixedPosition() && !entity.equals(this.entity);
	}

	private boolean isFarAwayEnought() {
		IF_Position_ImmutableView centeredEntityPosition = this.entity.getRealRectangle().getCenteredPosition();
		IF_Position_ImmutableView centeredGotoEntityPosition = this.gotoEntity.getRenderRectangle().getCenteredPosition();

		IF_XY absoluteDif = centeredEntityPosition.dif(centeredGotoEntityPosition).abs();
		IF_Dimension_ImmutableView entityHalfDimension = this.entity.getRealRectangle().getHalfDimension();
		IF_Dimension_ImmutableView gotoEntityHalfDimension = this.gotoEntity.getRealRectangle().getHalfDimension();

		boolean isNearHorizontal = absoluteDif.getX() < entityHalfDimension.getWidth() + gotoEntityHalfDimension.getWidth() + this.mapProvider.provide().getCollisionTileSize();
		boolean isNearVertical = absoluteDif.getY() < entityHalfDimension.getHeight() + gotoEntityHalfDimension.getHeight() + this.mapProvider.provide().getCollisionTileSize();

		return !isNearHorizontal || !isNearVertical;
	}

}
