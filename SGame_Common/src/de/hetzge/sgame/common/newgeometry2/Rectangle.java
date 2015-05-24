package de.hetzge.sgame.common.newgeometry2;

public class Rectangle implements IF_Rectangle_Mutable {

	private float centeredX;
	private float centeredY;

	private float width;
	private float height;

	public Rectangle(float centeredX, float centeredY, float width, float height) {
		this.centeredX = centeredX;
		this.centeredY = centeredY;
		this.width = width;
		this.height = height;
	}

	public Rectangle(float width, float height) {
		this(0f, 0f, width, height);
	}

	public Rectangle(float size) {
		this(size, size);
	}

	public Rectangle() {
	}

	public static Rectangle rectangle(float centeredX, float centeredY, float width, float height) {
		return new Rectangle(centeredX, centeredY, width, height);
	}

	public static Rectangle rectangle(float width, float height) {
		return new Rectangle(width, height);
	}

	public static Rectangle rectangle(float size) {
		return new Rectangle(size);
	}

	@Override
	public void setCenteredX(float centeredX) {
		this.centeredX = centeredX;
	}

	@Override
	public void setCenteredY(float centeredY) {
		this.centeredY = centeredY;
	}

	@Override
	public void setWidth(float width) {
		this.width = width;
	}

	@Override
	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public float getCenteredX() {
		return this.centeredX;
	}

	@Override
	public float getCenteredY() {
		return this.centeredY;
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
	public String toString() {
		return "Rectangle [centeredX=" + this.centeredX + ", centeredY=" + this.centeredY + ", width=" + this.width + ", height=" + this.height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.centeredX);
		result = prime * result + Float.floatToIntBits(this.centeredY);
		result = prime * result + Float.floatToIntBits(this.height);
		result = prime * result + Float.floatToIntBits(this.width);
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
		Rectangle other = (Rectangle) obj;
		if (Float.floatToIntBits(this.centeredX) != Float.floatToIntBits(other.centeredX)) {
			return false;
		}
		if (Float.floatToIntBits(this.centeredY) != Float.floatToIntBits(other.centeredY)) {
			return false;
		}
		if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height)) {
			return false;
		}
		if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width)) {
			return false;
		}
		return true;
	}

}
