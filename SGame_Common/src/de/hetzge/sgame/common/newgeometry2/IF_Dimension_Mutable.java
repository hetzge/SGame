package de.hetzge.sgame.common.newgeometry2;

public interface IF_Dimension_Mutable extends IF_XY_Mutable, IF_Dimension_Immutable {
	default void setWidth(float width) {
		this.getXY()._setX(width);
	}

	default void setHeight(float height) {
		this.getXY()._setY(height);
	}
}
