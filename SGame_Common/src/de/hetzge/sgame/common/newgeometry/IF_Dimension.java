package de.hetzge.sgame.common.newgeometry;

public interface IF_Dimension extends IF_XY<XY> {
	@Override
	public default void setWidth(float width) {
		this.setX(width);
	}

	@Override
	public default void setHeight(float height) {
		this.setY(height);
	}

	@Override
	public default float getWidth() {
		return Math.abs(this.getX());
	}

	@Override
	public default float getHeight() {
		return Math.abs(this.getY());
	}
}
