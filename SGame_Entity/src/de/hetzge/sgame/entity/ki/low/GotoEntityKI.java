package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.AStarService;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.PathfinderThread.PathfinderWorker;
import de.hetzge.sgame.common.Predicator;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.definition.IF_ReserveMap;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.service.MoveOnMapService;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.EntityOnMapService.IgnoreEntityCollisionWrapper;
import de.hetzge.sgame.entity.EntityOnMapService.On;
import de.hetzge.sgame.entity.EntityUtil;
import de.hetzge.sgame.entity.ki.BaseKI;

public class GotoEntityKI extends BaseKI {

	private final Entity gotoEntity;

	private final IF_MapProvider mapProvider = this.get(IF_MapProvider.class);
	private final EntityOnMapService entityOnMapService = this.get(EntityOnMapService.class);
	private final AStarService aStarService = this.get(AStarService.class);
	private final PathfinderThread pathfinderThread = this.get(PathfinderThread.class);
	private final MoveOnMapService moveOnMapService = this.get(MoveOnMapService.class);
	private final IF_ReserveMap reserveMap = this.get(IF_ReserveMap.class);
	private boolean initialized = false;

	private PathfinderWorker pathfinderWorker;

	public GotoEntityKI(Entity gotoEntity) {
		this.gotoEntity = gotoEntity;

		Log.KI.info("Created GotoKI for entity " + this.entity + " to " + gotoEntity);
	}

	@Override
	protected boolean callImpl() {
		if (!this.initialized) {
			return this.init();
		} else {
			return this.update();
		}
	}

	private boolean init() {
		IF_Map map = this.mapProvider.provide();
		ActiveCollisionMap fixEntityCollisionMap = map.getFixEntityCollisionMap();
		IgnoreEntityCollisionWrapper ignoreEntityCollisionWrapper = this.entityOnMapService.new IgnoreEntityCollisionWrapper(fixEntityCollisionMap, this.gotoEntity);

		IF_Coordinate_ImmutableView entityCollisionTilePosition = this.entityOnMapService.entityCollisionTileCenterCoordinate(this.entity);
		int startX = entityCollisionTilePosition.getIX();
		int startY = entityCollisionTilePosition.getIY();

		On on = this.entityOnMapService.on(this.gotoEntity.getRealRectangle());
		IF_Coordinate_ImmutableView goalCollisionCoordinate = on.findEmptyCoordinateAround(Predicator.of(this.entityOnMapService.CHECK_FLEXIBLE_COLLISION, this.entityOnMapService.CHECK_RESERVERD));
		if (goalCollisionCoordinate == null) {
			this.activeKICallback.onFailure();
			return false;
		}
		this.reserveMap.reserve(goalCollisionCoordinate, this.entity);

		int goalX = goalCollisionCoordinate.getIX();
		int goalY = goalCollisionCoordinate.getIY();

		this.pathfinderWorker = this.pathfinderThread.new PathfinderWorker() {
			@Override
			public Path findPath() {
				return GotoEntityKI.this.aStarService.findPath(map, ignoreEntityCollisionWrapper, startX, startY, goalX, goalY);
			}
		};

		this.initialized = true;
		return true;
	}

	private boolean update() {
		if (this.pathfinderWorker != null && this.pathfinderWorker.done()) {
			Path path = this.pathfinderWorker.get();
			this.pathfinderWorker = null;
			if (path.isPathNotPossible()) {
				this.activeKICallback.onFailure();
				return false;
			}
			this.moveOnMapService.setPath(this.entity, path);
		}

		this.moveOnMapService.move(this.entity);
		boolean goalReached = this.moveOnMapService.reachedGoal(this.entity);
		if (goalReached) {
			System.out.println("Is near enought: " + EntityUtil.isNearEnought(this.entity, this.gotoEntity));
			this.activeKICallback.onSuccess();
			return false;
		}

		return true;
	}

}
