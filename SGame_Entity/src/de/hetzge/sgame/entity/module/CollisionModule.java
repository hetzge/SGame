package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;

public class CollisionModule extends BaseEntityModule {

	private ActiveMap<Boolean> activeCollisionMap = new ActiveMap<Boolean>().setObjectInArea(true, 3, 5);

	public CollisionModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
		this.updateCollisionOnMap();
	}

	@Override
	public void updateImpl() {
	}

	/**
	 * registers the collision on the map
	 */
	public void updateCollisionOnMap() {
		if (this.entity.positionAndDimensionModuleCache.isAvailable()) {
			PositionAndDimensionModule module = this.entity.positionAndDimensionModuleCache.get();
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> positionAndDimensionRectangle = module.getPositionAndDimensionRectangle();

			int startCollisionTileX = Math.round(positionAndDimensionRectangle.getStartPosition().getX() / CommonConfig.INSTANCE.map.getCollisionTileSize());
			int startCollisionTileY = Math.round(positionAndDimensionRectangle.getStartPosition().getY() / CommonConfig.INSTANCE.map.getCollisionTileSize());

			if (module.isFixed()) {
				CommonConfig.INSTANCE.map.getFixEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, this.activeCollisionMap);
			} else {
				CommonConfig.INSTANCE.map.getFlexibleEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, this.activeCollisionMap);
			}
		}
	}

	/**
	 * set complete rectangle as collision
	 */
	// TODO WARNING wrong result if this happens before map sync
	public void setCollision(boolean collision) {
		if (this.entity.hasModule(PositionAndDimensionModule.class)) {
			PositionAndDimensionModule module = this.entity.getModule(PositionAndDimensionModule.class);
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> positionAndDimensionRectangle = module.getPositionAndDimensionRectangle();
			int widthInCollisionTiles = (int) Math.ceil(positionAndDimensionRectangle.getDimension().getWidth() / CommonConfig.INSTANCE.map.getCollisionTileSize());
			int heightInCollisionTiles = (int) Math.ceil(positionAndDimensionRectangle.getDimension().getHeight() / CommonConfig.INSTANCE.map.getCollisionTileSize());
			ActiveMap<Boolean> activeMap = new ActiveMap<>();
			activeMap.setObjectInArea(true, widthInCollisionTiles, heightInCollisionTiles);
			this.activeCollisionMap = activeMap;
		}
	}

	/**
	 * set given boolean array as collision tiles
	 */
	public void setCollision(boolean[][] collision) {
		if (collision.length == 0) {
			return;
		}

		int collisionWidthInTiles = collision.length;
		int collisionHeightInTiles = collision[0].length;

		ActiveMap<Boolean> activeMap = new ActiveMap<>();

		for (int x = 0; x < collisionWidthInTiles; x++) {
			for (int y = 0; y < collisionHeightInTiles; y++) {
				activeMap.setObjectOnPosition(collision[x][y], x, y);
			}
		}
	}

}
