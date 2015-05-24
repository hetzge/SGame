package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.service.MoveOnMapService;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.render.DefaultAnimationKey;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;
	private PathfinderWorker pathfinderWorker;

	private final IF_MapProvider mapProvider = this.get(IF_MapProvider.class);
	private final AStarService aStarService = this.get(AStarService.class);
	private final PathfinderThread pathfinderThread = this.get(PathfinderThread.class);
	private final EntityOnMapService entityOnMapService = this.get(EntityOnMapService.class);
	private final MoveOnMapService moveOnMapService = this.get(MoveOnMapService.class);

	private boolean initialized = false;

	public GotoKI(IF_Coordinate_Immutable goalCollisionTileCoordinate) {
		this(goalCollisionTileCoordinate.getColumn(), goalCollisionTileCoordinate.getRow());
	}

	public GotoKI(IF_Position_Immutable goalPosition) {
		IF_Coordinate_Immutable collisionTileGoal = this.mapProvider.provide().convertPxXYInCollisionTileXY(goalPosition);
		this.collisionTileGoalX = collisionTileGoal.getColumn();
		this.collisionTileGoalY = collisionTileGoal.getRow();

		Log.KI.info("Created GotoKI for entity " + this.entity + " to " + this.collisionTileGoalX + "/" + this.collisionTileGoalY);
	}

	public GotoKI(int collisionTileGoalX, int collisionTileGoalY) {
		this.collisionTileGoalX = collisionTileGoalX;
		this.collisionTileGoalY = collisionTileGoalY;

		Log.KI.info("Created GotoKI for entity " + this.entity + " to " + collisionTileGoalX + "/" + collisionTileGoalY);
	}

	@Override
	protected boolean callImpl() {
		if (this.isNotInitialized()) {
			return this.init();
		} else {
			return this.update();
		}
	}

	private boolean isNotInitialized() {
		return !this.initialized;
	}

	private boolean update() {
		if (this.pathfinderWorker != null && !this.pathfinderWorker.done()) {
			return true;
		}

		if (this.pathfinderWorker != null && this.pathfinderWorker.done()) {
			Path path = this.pathfinderWorker.get();
			try {
				if (this.pathfinderWorker.failure() || path.isPathNotPossible()) {
					this.activeKICallback.onFailure();
					return false;
				} else {
					Log.KI.info("Found path for entity " + this.entity + ": " + path);
					this.pathfinderWorker = null;
					this.moveOnMapService.setPath(this.entity, path);
				}
			} catch (NullPointerException ex) {
				throw new IllegalStateException(path + " / " + this.pathfinderWorker);
			}
		}

		this.entity.setAnimationKey(DefaultAnimationKey.WALK);
		this.moveOnMapService.move(this.entity);

		boolean goalReached = this.moveOnMapService.reachedGoal(this.entity);
		if (goalReached) {
			this.activeKICallback.onSuccess();
			return false;
		}

		return true;
	}

	private boolean init() {
		IF_Map map = this.mapProvider.provide();

		// test if goal is empty
		if (map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			this.activeKICallback.onFailure();
			return false;
		}

		IF_Coordinate_Immutable entityCollisionTilePosition = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.entity);
		int startX = entityCollisionTilePosition.getColumn();
		int startY = entityCollisionTilePosition.getRow();

		this.pathfinderWorker = this.pathfinderThread.new PathfinderWorker() {
			@Override
			public Path findPath() {
				// TODO replace with pathfind util
				// TODO check direct way
				// TODO entity collision
				return GotoKI.this.aStarService.findPath(map, startX, startY, GotoKI.this.collisionTileGoalX, GotoKI.this.collisionTileGoalY);
			}
		};

		this.initialized = true;

		return true;
	}
}
