package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_XY_View;

public interface IF_XY extends IF_XY_View {

	public float getX();

	public float getY();

	public void setX(float x);

	public void setY(float y);

	public default <T extends IF_XY> T add(T xy) {
		this.setX(this.getX() + xy.getX());
		this.setX(this.getY() + xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T substract(T xy) {
		this.setX(this.getX() - xy.getX());
		this.setX(this.getY() - xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T multiply(T xy) {
		this.setX(this.getX() * xy.getX());
		this.setX(this.getY() * xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T divide(T xy) {
		this.setX(this.getX() / xy.getX());
		this.setX(this.getY() / xy.getY());
		return (T) this;
	}

	public default float distance(IF_XY xy) {
		IF_XY abs = this.abs();
		return (float) Math.sqrt(abs.getX() * abs.getX() + abs.getY() * abs.getY());
	}

	public default IF_XY dif(IF_XY xy) {
		IF_XY copy = new XY(this);
		copy.substract(xy);
		copy.abs();
		return copy;
	}

	public default IF_XY abs() {
		return new XY(Math.abs(this.getX()), Math.abs(this.getY()));
	}

	public default <T extends IF_XY> T copy() {
		return (T) this;
	}
}
