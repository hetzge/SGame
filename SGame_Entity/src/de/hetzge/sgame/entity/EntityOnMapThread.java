package de.hetzge.sgame.entity;

import java.util.Collection;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
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
		Timer updateEntityOnMapTimer = new Timer(1000);

		while (true) {
			Util.sleep(100);
			if (updateEntityOnMapTimer.isTime()) {
				Collection<Entity> entities = this.entityPool.getEntities();
				for (Entity entity : entities) {
					this.updateEntityOnMap(entity);
					this.updateCollisionOnMap(entity);
				}
			}
		}
	}

	private void updateCollisionOnMap(Entity entity) {
		IF_Coordinate_ImmutableView entityStartCoordinate = this.entityOnMapService.entityCollisionTileStartCoordinate(entity);
		if (entity.isFixedPosition()) {
			this.mapProvider.provide().getFixEntityCollisionMap().connect(entityStartCoordinate.getIX(), entityStartCoordinate.getIY(), entity.getActiveCollisionMap());
		} else {
			this.mapProvider.provide().getFlexibleEntityCollisionMap().connect(entityStartCoordinate.getIX(), entityStartCoordinate.getIY(), entity.getActiveCollisionMap());
		}
	}

	private void updateEntityOnMap(Entity entity) {
		IF_Coordinate_ImmutableView entityCenteredCoordinate = this.entityOnMapService.entityTileCenterCoordinate(entity);
		this.activeEntityMap.connect(entityCenteredCoordinate.getIX(), entityCenteredCoordinate.getIY(), entity.getEntityOnMap());
	}
}
