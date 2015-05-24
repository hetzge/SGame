package de.hetzge.sgame.common.newgeometry2;

public interface IF_Position_Immutable extends IF_XY {
	default float getFX() {
		return this.getXY()._getX();
	}

	default float getFY() {
		return this.getXY()._getY();
	}
}
