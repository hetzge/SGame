package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;

public class CollisionModule extends BaseEntityModule {

	private ActiveMap<Boolean> activeCollisionMap = new ActiveMap<Boolean>().setObjectInArea(true, 3, 5);

	public CollisionModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
	}

	@Override
	public void updateImpl() {
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

	public ActiveMap<Boolean> getActiveCollisionMap() {
		return this.activeCollisionMap;
	}

}
