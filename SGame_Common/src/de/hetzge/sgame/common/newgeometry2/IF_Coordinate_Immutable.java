package de.hetzge.sgame.common.newgeometry2;

public interface IF_Coordinate_Immutable extends IF_XY {
	public default int getColumn() {
		return (int) this.getXY()._getX();
	}

	public default int getRow() {
		return (int) this.getXY()._getY();
	}
}