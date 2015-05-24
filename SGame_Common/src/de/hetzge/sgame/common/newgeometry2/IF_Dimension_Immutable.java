package de.hetzge.sgame.common.newgeometry2;

public interface IF_Dimension_Immutable extends IF_XY {
	default float getWidth() {
		return this.getXY()._getX();
	}

	default float getHeight() {
		return this.getXY()._getY();
	}
}
