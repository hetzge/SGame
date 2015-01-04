package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Position implements Serializable, IF_ImmutablePosition {

	private float x = 0;
	private float y = 0;

	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Position() {
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
		return "[" + this.x + "|" + this.y + "]";
	}
}
