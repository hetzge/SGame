package de.hetzge.sgame.entity;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class EntityService {

	private final IF_MapProvider mapProvider;

	public EntityService(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	public IF_ImmutablePosition<?> findPositionAround(Entity entity, IF_ImmutablePosition<?> from) {
		if (entity.positionAndDimensionModuleCache.isAvailable()) {
			return this.findPositionAround(entity.positionAndDimensionModuleCache.get(), from);
		} else {
			throw new IllegalArgumentException("The given entity has no position to evaluate position arround.");
		}
	}

	public IF_ImmutablePosition<?> findPositionAround(PositionAndDimensionModule positionAndDimensionModule, IF_ImmutablePosition<?> from) {
		IF_Map map = this.mapProvider.provide();
		float collisionTileSize = map.getCollisionTileSize();

		IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> rectangle = positionAndDimensionModule.getPositionAndDimensionRectangle();
		float startX = rectangle.getAX();
		float startY = rectangle.getAY();

	}

}
