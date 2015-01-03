package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Dimension implements Serializable {

	private float width;
	private float height;

	public Dimension(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public Dimension() {
	}

	public float getWidth() {
		return this.width;
	}

	public float calculateHalfWidth() {
		return this.width / 2;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return this.height;
	}

	public float calculateHalfHeight() {
		return this.height / 2;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
