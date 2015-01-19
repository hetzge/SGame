package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarUtil;
import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.KIConfig;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;
	private PathfinderWorker pathfinderWorker;

	public GotoKI(Entity entity, int collisionTileGoalX, int collisionTileGoalY) {
		super(entity);
		this.collisionTileGoalX = collisionTileGoalX;
		this.collisionTileGoalY = collisionTileGoalY;
	}

	@Override
	protected boolean condition() {
		return this.entity.positionAndDimensionModuleCache.isAvailable();
	}

	@Override
	protected KIState initImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();

		// test if goal is empty
		if (CommonConfig.INSTANCE.map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			return KIState.INIT_FAILURE;
		}

		int startX = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getX());
		int startY = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getY());

		this.pathfinderWorker = KIConfig.INSTANCE.pathfinderThread.new PathfinderWorker() {

			@Override
			public Path findPath() {

				// TODO replace with pathfind util
				// TODO check direct way
				// TODO entity collision
				return AStarUtil.findPath(CommonConfig.INSTANCE.map.getFixEntityCollisionMap(), startX, startY, GotoKI.this.collisionTileGoalX, GotoKI.this.collisionTileGoalY, new boolean[0][]);
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
