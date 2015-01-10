package de.hetzge.sgame.common.geometry;

public interface IF_ImmutablePrimitivRectangle {

	public float getX();

	public float getY();

	public float getWidth();

	public float getHeight();

	public default float getAX() {
		return this.getX() - this.getHalfWidth();
	}

	public default float getAY() {
		return this.getY() - this.getHalfHeight();
	}

	public default float getBX() {
		return this.getX() + this.getHalfWidth();
	}

	public default float getBY() {
		return this.getY() - this.getHalfHeight();
	}

	public default float getCX() {
		return this.getX() - this.getHalfWidth();
	}

	public default float getCY() {
		return this.getY() + this.getHalfHeight();
	}

	public default float getDX() {
		return this.getX() + this.getHalfWidth();
	}

	public default float getDY() {
		return this.getY() + this.getHalfHeight();
	}

	public default float getHalfWidth() {
		return this.getWidth() / 2;
	}

	public default float getHalfHeight() {
		return this.getHeight() / 2;
	}

	public default boolean doesOverlapWith(IF_ImmutablePrimitivRectangle otherRectangle) {
		return this.doesOverlapWith(this, otherRectangle) || this.doesOverlapWith(otherRectangle, this);
	}

	default boolean doesOverlapWith(IF_ImmutablePrimitivRectangle rectangle, IF_ImmutablePrimitivRectangle otherRectangle) {
		if (rectangle.doesOverlapWith(otherRectangle.getAX(), otherRectangle.getAY()))
			return true;
		if (rectangle.doesOverlapWith(otherRectangle.getBX(), otherRectangle.getBY()))
			return true;
		if (rectangle.doesOverlapWith(otherRectangle.getCX(), otherRectangle.getCY()))
			return true;
		if (rectangle.doesOverlapWith(otherRectangle.getDX(), otherRectangle.getDY()))
			return true;
		return false;
	}

	public default boolean doesOverlapWith(float x, float y) {
		return x > this.getAX() && x < this.getDX() && y > this.getAY() && y < this.getDY();
	}

}
