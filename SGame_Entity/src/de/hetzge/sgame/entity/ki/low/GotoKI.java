package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarUtil;
import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;

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
	protected KIState updateImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();
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
	protected KIState initImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();

		// test if goal is empty
		if (CommonConfig.INSTANCE.map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			return KIState.INIT_FAILURE;
		}

		int startX = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getX());
		int startY = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getY());

		// TODO replace with pathfind util
		// TODO entity collision
		Path path = AStarUtil.findPath(CommonConfig.INSTANCE.map.getFixEntityCollisionMap(), startX, startY, this.collisionTileGoalX, this.collisionTileGoalY, new boolean[0][]);

		if (path.isPathNotPossible()) {
			return KIState.INIT_FAILURE;
		}

		positionAndDimensionModule.setPath(path);

		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.positionAndDimensionModuleCache.get();
		positionAndDimensionModule.unsetPath();
	}
}
