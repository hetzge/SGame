package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;
	private PathfinderWorker pathfinderWorker;

	private final IF_MapProvider mapProvider = this.get(IF_MapProvider.class);
	private final AStarService aStarService = this.get(AStarService.class);
	private final PathfinderThread pathfinderThread = this.get(PathfinderThread.class);

	public GotoKI(Entity entity, IF_Position_ImmutableView goalPosition) {
		super(entity);

		IF_Coordinate collisionTileGoal = this.mapProvider.provide().convertPxXYInCollisionTileXY(goalPosition);
		this.collisionTileGoalX = collisionTileGoal.getIX();
		this.collisionTileGoalY = collisionTileGoal.getIY();

		Log.KI.debug("Created GotoKI for entity " + entity + " to " + this.collisionTileGoalX + "/" + this.collisionTileGoalY);
	}

	public GotoKI(Entity entity, int collisionTileGoalX, int collisionTileGoalY) {
		super(entity);

		this.collisionTileGoalX = collisionTileGoalX;
		this.collisionTileGoalY = collisionTileGoalY;

		Log.KI.debug("Created GotoKI for entity " + entity + " to " + collisionTileGoalX + "/" + collisionTileGoalY);
	}

	@Override
	protected boolean condition() {
		return true;
	}

	@Override
	protected KIState initImpl() {
		IF_Map map = this.mapProvider.provide();

		// test if goal is empty
		if (map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			return KIState.INIT_FAILURE;
		}

		IF_Position_ImmutableView entityCenteredPosition = this.entity.getCenteredPosition();
		IF_Coordinate entityCollisionTilePosition = map.convertPxXYInCollisionTileXY(entityCenteredPosition);

		int startX = entityCollisionTilePosition.getIX();
		int startY = entityCollisionTilePosition.getIY();

		this.pathfinderWorker = this.pathfinderThread.new PathfinderWorker() {
			@Override
			public Path findPath() {
				// TODO replace with pathfind util
				// TODO check direct way
				// TODO entity collision
				return GotoKI.this.aStarService.findPath(map.getFixEntityCollisionMap(), startX, startY, GotoKI.this.collisionTileGoalX, GotoKI.this.collisionTileGoalY, new boolean[0][0]);
			}
		};

		return KIState.ACTIVE;
	}

	@Override
	protected KIState updateImpl() {
		if (this.pathfinderWorker != null && !this.pathfinderWorker.done()) {
			return KIState.ACTIVE;
		}

		if (this.pathfinderWorker != null && this.pathfinderWorker.done()) {
			Path path = this.pathfinderWorker.get();
			Log.KI.debug("Found path for entity " + this.entity + ": " + path);
			this.pathfinderWorker = null;
			if (path.isPathNotPossible()) {
				return KIState.FAILURE;
			}
			this.entity.setPath(path);
		}

		if (!this.entity.hasPath()) {
			return KIState.FAILURE;
		}

		if (this.entity.reachedEndOfPath()) {
			return KIState.SUCCESS;
		}

		this.entity.continueOnPath();
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
		this.entity.unsetPath();
	}
}
