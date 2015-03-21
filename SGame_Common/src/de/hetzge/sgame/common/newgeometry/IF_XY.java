package de.hetzge.sgame.common.newgeometry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import de.hetzge.sgame.common.Orientation;

public interface IF_XY extends Serializable {

	public float getX();

	public float getY();

	public default <T extends IF_XY> T setX(float x) {
		throw new UnsupportedOperationException();
	}

	public default <T extends IF_XY> T setY(float y) {
		throw new UnsupportedOperationException();
	}

	public default <T extends IF_XY> T add(T xy) {
		this.setX(this.getX() + xy.getX());
		this.setY(this.getY() + xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T substract(T xy) {
		this.setX(this.getX() - xy.getX());
		this.setY(this.getY() - xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T multiply(T xy) {
		this.setX(this.getX() * xy.getX());
		this.setY(this.getY() * xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T divide(T xy) {
		this.setX(this.getX() / xy.getX());
		this.setY(this.getY() / xy.getY());
		return (T) this;
	}

	public default <T extends IF_XY> T abs() {
		this.setX(Math.abs(this.getX()));
		this.setY(Math.abs(this.getY()));
		return (T) this;
	}

	public default float distance(IF_XY xy) {
		IF_XY abs = this.dif(xy).abs();
		return (float) Math.sqrt(abs.getX() * abs.getX() + abs.getY() * abs.getY());
	}

	public default IF_XY dif(IF_XY xy) {
		IF_XY copy = this.copy();
		copy.substract(xy);
		copy.abs();
		return copy;
	}

	/**
	 * befindet sich <Orientation>lich von <other> 
	 * nur einfache Orientation
	 */
	public default Orientation orientationToOther(IF_XY other) {
		IF_XY dif = other.copy().dif(this);
		if (dif.getX() > dif.getY()) {
			if (this.getX() - other.getX() > 0) {
				return Orientation.EAST;
			} else {
				return Orientation.WEST;
			}
		} else {
			if (this.getY() - other.getY() > 0) {
				return Orientation.SOUTH;
			} else {
				return Orientation.NORTH;
			}
		}
	}

	public default IF_XY copy() {
		try {
			return  getClass().getConstructor(float.class, float.class).newInstance(this.getX(), this.getY());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException("Every implementation (int this case " + getClass() + ") of IF_XY must have a constructor with float x and float y as parameter.", e);
		}
	}
}
