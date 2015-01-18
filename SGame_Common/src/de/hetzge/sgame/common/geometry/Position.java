package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

import de.hetzge.sgame.common.Util;

public class Position implements Serializable, IF_Position<Position> {

	private float x = 0f;
	private float y = 0f;

	public Position() {
	}

	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Position(IF_ImmutablePosition<?> otherPosition) {
		this(otherPosition.getX(), otherPosition.getY());
	}

	public Position add(Position otherPosition) {
		this.x += otherPosition.getX();
		this.y += otherPosition.getY();
		return this;
	}

	public Position subtract(Position otherPosition) {
		this.x -= otherPosition.getX();
		this.y -= otherPosition.getY();
		return this;
	}

	public Position multiply(Position otherPosition) {
		this.x *= otherPosition.getX();
		this.y *= otherPosition.getY();
		return this;
	}

	public Position divide(Position otherPosition) {
		this.x /= otherPosition.getX();
		this.y /= otherPosition.getY();
		return this;
	}

	@Override
	public Position copy() {
		return new Position(this.x, this.y);
	}

	@Override
	public float distance(IF_ImmutablePosition<?> otherPosition) {
		return Util.calculateDistance(this, otherPosition);
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + this.getX() + "|" + this.getY() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x);
		result = prime * result + Float.floatToIntBits(this.y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

}
