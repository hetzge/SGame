package de.hetzge.sgame.common.newgeometry2;

public interface IF_Coordinate_Mutable extends IF_XY_Mutable, IF_Coordinate_Immutable {
	public default void setColumn(int x) {
		this.getXY()._setX(x);
	}

	public default void setRow(int y) {
		this.getXY()._setY(y);
	}
}
