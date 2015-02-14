package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;
	private PathfinderWorker pathfinderWorker;

	private final IF_MapProvider mapProvider = this.get(IF_MapProvider.class);
	private final AStarService aStarService = this.get(AStarService.class);
	private final PathfinderThread pathfinderThread = this.get(PathfinderThread.class);

	public GotoKI(Entity entity, IF_Position_ImmutableView goalPosition) {
		super(entity);

		this.collisionTileGoalX = this.mapProvider.provide().convertPxInCollisionTile(goalPosition.getX());
		this.collisionTileGoalY = this.mapProvider.provide().convertPxInCollisionTile(goalPosition.getY());

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
		return this.entity.positionAndDimensionModuleCache.isAvailable();
	}

	@Override
	protected KIState initImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();
		IF_Map map = this.mapProvider.provide();

		// test if goal is empty
		if (map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			return KIState.INIT_FAILURE;
		}

		int startX = map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getCenteredPosition().getX());
		int startY = map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getCenteredPosition().getY());

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
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();

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
			positionAndDimensionModule.setPath(path);
		}

		if (!positionAndDimensionModule.hasPath()) {
			return KIState.FAILURE;
		}

		if (positionAndDimensionModule.reachedEndOfPath()) {
			return KIState.SUCCESS;
		}

		positionAndDimensionModule.continueOnPath();
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();
		positionAndDimensionModule.unsetPath();
	}
}
