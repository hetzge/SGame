package de.hetzge.sgame.common.newgeometry;

public interface IF_Position extends IF_XY<XY> {
	@Override
	public default float getFX() {
		return this.getX();
	}

	@Override
	public default float getFY() {
		return this.getY();
	}

	@Override
	public default void setFX(float x) {
		this.setX(x);
	}

	@Override
	public default void setFY(float y) {
		this.setY(y);
	}
}
