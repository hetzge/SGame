package de.hetzge.sgame.common;

import java.io.Serializable;

import de.hetzge.sgame.common.definition.IF_Collision;

public class BaseCollisionImpl implements IF_Collision, Serializable {

	private final boolean[][] collision;
	private final int widthInTiles;
	private final int heightInTiles;

	public BaseCollisionImpl(int widthInTiles, int heightInTiles) {
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.collision = new boolean[widthInTiles][];
		for (int x = 0; x < widthInTiles; x++) {
			this.collision[x] = new boolean[heightInTiles];
		}
	}

	@Override
	public int getWidthInTiles() {
		return this.widthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.heightInTiles;
	}

	@Override
	public boolean isCollision(int x, int y) {
		return this.collision[x][y];
	}

	@Override
	public void setCollision(int x, int y, boolean collision) {
		this.collision[x][y] = collision;
	}

}
