package de.hetzge.sgame.entity;

import java.util.Set;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
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
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> positionAndDimensionRectangle = module.getPositionAndDimensionRectangle();

			int startCollisionTileX = this.mapProvider.provide().convertPxInCollisionTile(positionAndDimensionRectangle.getStartPosition().getX());
			int startCollisionTileY = this.mapProvider.provide().convertPxInCollisionTile(positionAndDimensionRectangle.getStartPosition().getY());

			if (module.isFixed()) {
				this.mapProvider.provide().getFixEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, positionAndDimensionModule.getActiveCollisionMap());
			} else {
				this.mapProvider.provide().getFlexibleEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, positionAndDimensionModule.getActiveCollisionMap());
			}
		}
	}

	private void updateEntityOnMap(PositionAndDimensionModule positionAndDimensionModule) {
		IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> rectangle = positionAndDimensionModule.getPositionAndDimensionRectangle();
		int x = this.mapProvider.provide().convertPxInTile(rectangle.getX());
		int y = this.mapProvider.provide().convertPxInTile(rectangle.getY());
		this.activeEntityMap.connect(x, y, positionAndDimensionModule.getEntityOnMap());
	}
}
