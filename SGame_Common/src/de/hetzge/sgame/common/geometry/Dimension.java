package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Dimension implements Serializable, IF_Dimension<Dimension> {

	private float width;
	private float height;

	public Dimension() {
	}

	public Dimension(IF_ImmutablePosition<?> position) {
		this(Math.abs(position.getX()), Math.abs(position.getY()));
	}

	public Dimension(float width, float height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public float getWidth() {
		return this.width;
	}

	@Override
	public float getHeight() {
		return this.height;
	}

	@Override
	public float calculateHalfWidth() {
		return this.width / 2;
	}

	@Override
	public float calculateHalfHeight() {
		return this.height / 2;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "[" + this.getWidth() + "|" + this.getHeight() + "]";
	}
}
