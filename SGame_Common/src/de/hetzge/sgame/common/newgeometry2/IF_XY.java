package de.hetzge.sgame.common.newgeometry2;

import java.io.Serializable;

import de.hetzge.sgame.common.Orientation;

public interface IF_XY extends Serializable{

	XY getXY();

	default Orientation orientationTo(IF_XY other) {
		IF_XY dif = other.dif(this);
		if (dif.getXY().getFX() > dif.getXY().getFY()) {
			if (this.getXY().getFX() - other.getXY().getFX() > 0) {
				return Orientation.EAST;
			} else {
				return Orientation.WEST;
			}
		} else {
			if (this.getXY().getFY() - other.getXY().getFY() > 0) {
				return Orientation.SOUTH;
			} else {
				return Orientation.NORTH;
			}
		}
	}

	default XY copy() {
		return new XY(this.getXY()._getX(), this.getXY()._getY());
	}

	default XY dif(IF_XY xy) {
		return new XY(this.getXY()._getX() - xy.getXY()._getX(), this.getXY()._getY() - xy.getXY()._getY());
	}

	default float distance(IF_XY xy) {
		float xDiv = Math.abs(this.getXY()._getX() - xy.getXY()._getX());
		float yDiv = Math.abs(this.getXY()._getY() - xy.getXY()._getY());
		return (float) Math.sqrt(xDiv * xDiv + yDiv * yDiv);
	}

}
