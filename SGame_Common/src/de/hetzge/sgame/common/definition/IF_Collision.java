package de.hetzge.sgame.common.definition;

public interface IF_Collision {

	public int getWidthInTiles();

	public int getHeightInTiles();

	public boolean isCollision(int x, int y);

	public boolean setCollision(int x, int y, boolean collision);

	public default void setCollision(int startX, int startY, IF_Collision collision) {
		int endX = startX + collision.getWidthInTiles();
		int endY = startY + collision.getHeightInTiles();

		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;

		if (endX > this.getWidthInTiles())
			endX = this.getWidthInTiles();
		if (endY > this.getHeightInTiles())
			endY = this.getHeightInTiles();

		int x2 = 0;
		for (int x = startX; x < endX; x++) {
			int y2 = 0;
			for (int y = startY; y < endY; y++) {
				this.setCollision(x, y, collision.isCollision(x2, y2));
				y2++;
			}
			x2++;
		}

	}
}
