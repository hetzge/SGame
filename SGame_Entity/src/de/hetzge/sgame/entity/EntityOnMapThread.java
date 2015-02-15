package de.hetzge.sgame.entity;

import java.util.Set;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.timer.Timer;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class EntityOnMapThread extends Thread {

	private final EntityPool entityPool;
	private final IF_MapProvider mapProvider;
	private final ActiveEntityMap activeEntityMap;

	public EntityOnMapThread(EntityPool entityPool, IF_MapProvider mapProvider, ActiveEntityMap activeEntityMap) {
		super("entity_on_map_thread");
		this.entityPool = entityPool;
		this.mapProvider = mapProvider;
		this.activeEntityMap = activeEntityMap;
	}

	@Override
	public void run() {
		Timer updateEntityOnMapTimer = new Timer(1000);

		while (true) {
			Util.sleep(100);
			if (updateEntityOnMapTimer.isTime()) {
				Set<PositionAndDimensionModule> positionAndDimensionModules = this.entityPool.getEntityModulesByModuleClass(PositionAndDimensionModule.class);
				for (PositionAndDimensionModule positionAndDimensionModule : positionAndDimensionModules) {
					this.updateEntityOnMap(positionAndDimensionModule);
					this.updateCollisionOnMap(positionAndDimensionModule);
				}
			}
		}
	}

	private void updateCollisionOnMap(PositionAndDimensionModule positionAndDimensionModule) {
		if (positionAndDimensionModule.entity.positionAndDimensionModuleCache.isAvailable()) {
			PositionAndDimensionModule module = positionAndDimensionModule.entity.positionAndDimensionModuleCache.get();
			IF_Rectangle_ImmutableView positionAndDimensionRectangle = module.getPositionAndDimensionRectangle();

			IF_Coordinate entityStartCoordinate = this.mapProvider.provide().convertPxXYInCollisionTileXY(positionAndDimensionRectangle.getPositionA().asPositionImmutableView());

			if (module.isFixed()) {
				this.mapProvider.provide().getFixEntityCollisionMap().connect(entityStartCoordinate.getIX(), entityStartCoordinate.getIY(), positionAndDimensionModule.getActiveCollisionMap());
			} else {
				this.mapProvider.provide().getFlexibleEntityCollisionMap().connect(entityStartCoordinate.getIX(), entityStartCoordinate.getIY(), positionAndDimensionModule.getActiveCollisionMap());
			}
		}
	}

	private void updateEntityOnMap(PositionAndDimensionModule positionAndDimensionModule) {
		IF_Rectangle_ImmutableView rectangle = positionAndDimensionModule.getPositionAndDimensionRectangle();
		IF_Coordinate entityCenteredCoordinate = this.mapProvider.provide().convertPxXYInTileXY(rectangle.getCenteredPosition());
		this.activeEntityMap.connect(entityCenteredCoordinate.getIX(), entityCenteredCoordinate.getIY(), positionAndDimensionModule.getEntityOnMap());
	}
}
