package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_XY_View;

public interface IF_XY<IMPLEMENTATION extends IF_XY<?>> extends IF_XY_View, IF_Coordinate_ImmutableView, IF_Coordinate_MutableView, IF_Dimension_ImmutableView, IF_Dimension_MutableView, IF_Position_ImmutableView, IF_Position_MutableView {

	public float getX();

	public float getY();

	public void setX(float x);

	public void setY(float y);

	@Override
	public default IF_Dimension_ImmutableView asDimensionImmutableView() {
		return this;
	}

	@Override
	public default IF_Dimension_MutableView asDimensionMutableView() {
		return this;
	}

	@Override
	public default IF_Position_ImmutableView asPositionImmutableView() {
		return this;
	}

	@Override
	public default IF_Position_MutableView asPositionMutableView() {
		return this;
	}

	@Override
	public default IF_Coordinate_ImmutableView asCoordinateImmutableView() {
		return this;
	}

	@Override
	public default IF_Coordinate_MutableView asCoordinateMutableView() {
		return this;
	}

	public default void add(IF_XY<?> xy) {
		this.setX(this.getX() + xy.getX());
		this.setX(this.getY() + xy.getY());
	}

	public default void substract(IF_XY<?> xy) {
		this.setX(this.getX() - xy.getX());
		this.setX(this.getY() - xy.getY());
	}

	public default void multiply(IF_XY<?> xy) {
		this.setX(this.getX() * xy.getX());
		this.setX(this.getY() * xy.getY());
	}

	public default void divide(IF_XY<?> xy) {
		this.setX(this.getX() / xy.getX());
		this.setX(this.getY() / xy.getY());
	}

	public default float distance(IF_XY<?> xy) {
		IMPLEMENTATION abs = this.abs();
		return (float) Math.sqrt(abs.getX() * abs.getX() + abs.getY() * abs.getY());
	}

	public default IMPLEMENTATION dif(IF_XY<?> xy) {
		IMPLEMENTATION copy = this.copy();
		copy.substract(xy);
		copy.abs();
		return copy;
	}

	public default IMPLEMENTATION abs() {
		return this.create(Math.abs(this.getX()), Math.abs(this.getY()));
	}

	public IMPLEMENTATION copy();

	public IMPLEMENTATION create(float x, float y);

	public IMPLEMENTATION create(IF_XY<?> xy);

}
