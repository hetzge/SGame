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

	public XY(float value){
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

}
