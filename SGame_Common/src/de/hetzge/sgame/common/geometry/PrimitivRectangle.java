package de.hetzge.sgame.common.geometry;

public class PrimitivRectangle implements IF_ImmutablePrimitivRectangle {

	private float x;
	private float y;

	private float width;
	private float height;

	public PrimitivRectangle() {
		this(0f, 0f, 0f, 0f);
	}

	public PrimitivRectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public float getWidth() {
		return this.width;
	}

	@Override
	public float getHeight() {
		return this.height;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
