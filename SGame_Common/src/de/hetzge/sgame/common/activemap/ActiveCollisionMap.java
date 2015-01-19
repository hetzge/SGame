package de.hetzge.sgame.common.activemap;

import java.util.Collection;

import de.hetzge.sgame.common.definition.IF_Collision;

public class ActiveCollisionMap extends ActiveMap<Boolean> implements IF_Collision {

	private final int widthInTiles;
	private final int heightInTiles;

	public ActiveCollisionMap(int widthInTiles, int heightInTiles) {
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
	}

	@Override
	public boolean isCollision(int x, int y) {
		Collection<Boolean> connectedObjects = this.getConnectedObjects(x, y);
		for (Boolean aBoolean : connectedObjects) {
			if (aBoolean) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setCollision(int x, int y, boolean collision) {
		throw new UnsupportedOperationException("Connect other active map to set collision on ActiveCollisionMap");
	}

	@Override
	public int getWidthInTiles() {
		return this.widthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.heightInTiles;
	}

}
