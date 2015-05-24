package de.hetzge.sgame.common.newgeometry2;

import java.io.Serializable;

public interface IF_Rectangle_Immutable extends Serializable {

	float getCenteredX();

	float getCenteredY();

	float getWidth();

	float getHeight();

	default float getAX() {
		return this.getCenteredX() - this.getWidth() / 2;
	}

	default float getAY() {
		return this.getCenteredY() - this.getHeight() / 2;
	}

	default float getBX() {
		return this.getCenteredX() + this.getWidth() / 2;
	}

	default float getBY() {
		return this.getCenteredY() - this.getHeight() / 2;
	}

	default float getCX() {
		return this.getCenteredX() - this.getWidth() / 2;
	}

	default float getCY() {
		return this.getCenteredY() + this.getHeight() / 2;
	}

	default float getDX() {
		return this.getCenteredX() + this.getWidth() / 2;
	}

	default float getDY() {
		return this.getCenteredY() + this.getHeight() / 2;
	}

	default IF_Position_Immutable getA() {
		return new XY(this.getAX(), this.getAY());
	}

	default IF_Position_Immutable getB() {
		return new XY(this.getBX(), this.getBY());
	}

	default IF_Position_Immutable getC() {
		return new XY(this.getCX(), this.getCY());
	}

	default IF_Position_Immutable getD() {
		return new XY(this.getDX(), this.getDY());
	}

	default IF_Position_Immutable getCenter() {
		return new XY(this.getCenteredX(), this.getCenteredY());
	}

	default IF_Dimension_Immutable getDimension() {
		return new XY(this.getWidth(), this.getHeight());
	}

	default boolean doesOverlapWith(IF_Rectangle_Immutable otherRectangle) {
		return this.doesOverlapWith(this, otherRectangle) || this.doesOverlapWith(otherRectangle, this);
	}

	default boolean doesOverlapWith(IF_Rectangle_Immutable rectangle, IF_Rectangle_Immutable otherRectangle) {
		float ax = otherRectangle.getAX();
		float ay = otherRectangle.getAY();
		if (rectangle.doesOverlapWith(ax, ay)) {
			return true;
		}
		float bx = otherRectangle.getBX();
		float by = otherRectangle.getBY();
		if (rectangle.doesOverlapWith(bx, by)) {
			return true;
		}
		float cx = otherRectangle.getCX();
		float cy = otherRectangle.getCY();
		if (rectangle.doesOverlapWith(cx, cy)) {
			return true;
		}
		float dx = otherRectangle.getDX();
		float dy = otherRectangle.getDY();
		if (rectangle.doesOverlapWith(dx, dy)) {
			return true;
		}
		return false;
	}

	default boolean doesOverlapWith(float x, float y) {
		float ax = this.getAX();
		float ay = this.getAY();

		float dx = this.getDX();
		float dy = this.getDY();

		return x > ax && x < dx && y > ay && y < dy;
	}

	default IF_Dimension_Immutable getHalfDimension() {
		IF_Dimension_Mutable copy = new XY(this.getWidth(), this.getHeight());
		IF_Dimension_Mutable halfDimension = copy.divide(new XY(2, 2));
		return halfDimension;
	}

}
