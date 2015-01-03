package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class CollisionModule extends BaseEntityModule {

	private final SyncProperty<ActiveMap<Boolean>> activeCollisionMap = new SyncProperty<ActiveMap<Boolean>>(new ActiveMap<>(0, 0));

	public CollisionModule(Entity entity) {
		super(entity);
	}

	@Override
	public void init() {
		this.updateCollisionOnMap();
	}

	@Override
	public void update() {
	}

	public void setCollision(boolean collision) {
		if (this.entity.hasModule(PositionAndDimensionModule.class)) {
			PositionAndDimensionModule module = this.entity.getModule(PositionAndDimensionModule.class);
			Rectangle positionAndDimensionRectangle = module.dimensionSyncProperty.getValue();
			int widthInTiles = (int) Math.ceil(positionAndDimensionRectangle.getDimension().getWidth() / CommonConfig.INSTANCE.collisionTileSize);
			int heightInTiles = (int) Math.ceil(positionAndDimensionRectangle.getDimension().getHeight() / CommonConfig.INSTANCE.collisionTileSize);
			ActiveMap<Boolean> activeMap = new ActiveMap<>(widthInTiles, heightInTiles);
			activeMap.setObject(true);
			this.activeCollisionMap.setValue(activeMap);
		}
	}

	public void updateCollisionOnMap() {
		if (this.entity.hasModule(PositionAndDimensionModule.class)) {
			PositionAndDimensionModule module = this.entity.getModule(PositionAndDimensionModule.class);

		}
	}

	public void setCollision(boolean[][] collision) {
		if (collision.length == 0)
			return;

		int collisionWidthInTiles = collision.length;
		int collisionHeightInTiles = collision[0].length;

		ActiveMap<Boolean> activeMap = new ActiveMap<>(collisionWidthInTiles, collisionHeightInTiles);

		for (int x = 0; x < collisionWidthInTiles; x++) {
			for (int y = 0; y < collisionHeightInTiles; y++) {
				activeMap.setObject(x, y, collision[x][y]);
			}
		}
	}

}
