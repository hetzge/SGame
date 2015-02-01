package de.hetzge.sgame.common.newgeometry;

public interface IF_Coordinate extends IF_XY<XY> {
	@Override
	public default void setIX(int x) {
		this.setX(x);
	}

	@Override
	public default void setIY(int y) {
		this.setY(y);
	}

	@Override
	public default int getIX() {
		return (int) Math.floor(this.getX());
	}

	@Override
	public default int getIY() {
		return (int) Math.floor(this.getY());
	}
}
