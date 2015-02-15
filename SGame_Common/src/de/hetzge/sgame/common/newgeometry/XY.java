package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_MutableView;

public class XY implements IF_Position, IF_Dimension, IF_Coordinate {

	private float x;
	private float y;

	public XY(float value) {
		this.x = value;
		this.y = value;
	}

	public XY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public XY(IF_XY xy) {
		this.x = xy.getX();
		this.y = xy.getY();
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public <T extends IF_XY> T setX(float x) {
		this.x = x;
		return (T) this;
	}

	@Override
	public <T extends IF_XY> T setY(float y) {
		this.y = y;
		return (T) this;
	}

	@Override
	public IF_Dimension_ImmutableView asDimensionImmutableView() {
		return this;
	}

	@Override
	public IF_Dimension_MutableView asDimensionMutableView() {
		return this;
	}

	@Override
	public IF_Position_ImmutableView asPositionImmutableView() {
		return this;
	}

	@Override
	public IF_Position_MutableView asPositionMutableView() {
		return this;
	}

	@Override
	public IF_Coordinate_ImmutableView asCoordinateImmutableView() {
		return this;
	}

	@Override
	public IF_Coordinate_MutableView asCoordinateMutableView() {
		return this;
	}

	@Override
	public IF_XY copy() {
		return new XY(this.getFX(), this.getFY());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x);
		result = prime * result + Float.floatToIntBits(this.y);
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
		XY other = (XY) obj;
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

}
