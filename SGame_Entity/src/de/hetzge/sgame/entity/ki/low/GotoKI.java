package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.KIConfig;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class GotoKI extends BaseKI {

	private final int collisionTileGoalX;
	private final int collisionTileGoalY;
	private PathfinderWorker pathfinderWorker;

	private final IF_MapProvider mapProvider;
	private final AStarService aStarService;

	public GotoKI(Entity entity, int collisionTileGoalX, int collisionTileGoalY) {
		super(entity);
		this.collisionTileGoalX = collisionTileGoalX;
		this.collisionTileGoalY = collisionTileGoalY;

		this.mapProvider = this.get(IF_MapProvider.class);
		this.aStarService = this.get(AStarService.class);
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

		int startX = map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getX());
		int startY = map.convertPxInCollisionTile(positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition().getY());

		this.pathfinderWorker = KIConfig.INSTANCE.pathfinderThread.new PathfinderWorker() {

			@Override
			public Path findPath() {

				// TODO replace with pathfind util
				// TODO check direct way
				// TODO entity collision
				return GotoKI.this.aStarService.findPath(map.getFixEntityCollisionMap(), startX, startY, GotoKI.this.collisionTileGoalX, GotoKI.this.collisionTileGoalY, new boolean[0][]);
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
