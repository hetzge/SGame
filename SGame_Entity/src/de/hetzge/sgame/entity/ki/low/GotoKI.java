package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarUtil;
import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.IF_LowLevelKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class GotoKI implements IF_LowLevelKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;

	public GotoKI(int collisionTileGoalX, int collisionTileGoalY) {
		this.collisionTileGoalX = collisionTileGoalX;
		this.collisionTileGoalY = collisionTileGoalY;
	}

	@Override
	public boolean condition(Entity entity) {
		if (entity.positionAndDimensionModuleCache.isNotAvailable()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean init(Entity entity) {
		PositionAndDimensionModule positionAndDimensionModule = entity.positionAndDimensionModuleCache.get();

		// test if goal is empty
		if (CommonConfig.INSTANCE.map.getFixEntityCollisionMap().isCollision(this.collisionTileGoalX, this.collisionTileGoalY)) {
			return false;
		}

		int startX = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getX());
		int startY = CommonConfig.INSTANCE.map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getY());

		// TODO replace with pathfind util
		// TODO entity collision
		Path path = AStarUtil.findPath(CommonConfig.INSTANCE.map.getFixEntityCollisionMap(), startX, startY, this.collisionTileGoalX, this.collisionTileGoalY, new boolean[0][]);

		if (path.isPathNotPossible()) {
			return false;
		}

		positionAndDimensionModule.setPath(path);

		return true;
	}

	@Override
	public boolean call(Entity entity) {
		PositionAndDimensionModule positionAndDimensionModule = entity.positionAndDimensionModuleCache.get();
		if (!positionAndDimensionModule.hasPath()) {
			return false;
		}

		positionAndDimensionModule.continueOnPath();
		return true;
	}

	@Override
	public void finish(Entity entity) {
		PositionAndDimensionModule positionAndDimensionModule = entity.positionAndDimensionModuleCache.get();
		positionAndDimensionModule.unsetPath();
	}
}
