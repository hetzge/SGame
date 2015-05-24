package de.hetzge.sgame.common.newgeometry2;


public interface IF_Position_Mutable extends IF_XY_Mutable, IF_Position_Immutable {
	default void setFX(float x) {
		this.getXY()._setX(x);
	}

	default void setFY(float y) {
		this.getXY()._setY(y);
	}
}
