package de.hetzge.sgame.entity;

import java.util.Collection;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.timer.Timer;

public class EntityOnMapThread extends Thread {

	private final EntityPool entityPool;
	private final IF_MapProvider mapProvider;
	private final ActiveEntityMap activeEntityMap;
	private final EntityOnMapService entityOnMapService;

	public EntityOnMapThread(EntityPool entityPool, IF_MapProvider mapProvider, ActiveEntityMap activeEntityMap, EntityOnMapService entityOnMapService) {
		super("entity_on_map_thread");
		this.entityPool = entityPool;
		this.mapProvider = mapProvider;
		this.activeEntityMap = activeEntityMap;
		this.entityOnMapService = entityOnMapService;
	}

	@Override
	public void run() {
		Timer updateEntityOnMapTimer = new Timer(100);

		while (true) {
			Util.sleep(1000);
			if (updateEntityOnMapTimer.isTime()) {
				Collection<Entity> entities = this.entityPool.getEntities();
				for (Entity entity : entities) {

					// TODO this staff could be done everytime the position of
					// entity changes
					this.updateEntityOnMap(entity);
					this.updateCollisionOnMap(entity);
				}
			}
		}
	}

	private void updateCollisionOnMap(Entity entity) {
		IF_Coordinate_Immutable entityStartCoordinate = this.entityOnMapService.entityCollisionTileStartCoordinate(entity);
		if (entity.isFixedPosition()) {
			this.mapProvider.provide().getFixEntityCollisionMap().connect(entityStartCoordinate.getColumn(), entityStartCoordinate.getRow(), entity.getActiveCollisionMap());
		} else {
			this.mapProvider.provide().getFlexibleEntityCollisionMap().connect(entityStartCoordinate.getColumn(), entityStartCoordinate.getRow(), entity.getActiveCollisionMap());
		}
	}

	private void updateEntityOnMap(Entity entity) {
		IF_Coordinate_Immutable entityCenteredCoordinate = this.entityOnMapService.entityTileCenterCoordinate(entity);
		this.activeEntityMap.connect(entityCenteredCoordinate.getColumn(), entityCenteredCoordinate.getRow(), entity.getEntityOnMap());
	}

}