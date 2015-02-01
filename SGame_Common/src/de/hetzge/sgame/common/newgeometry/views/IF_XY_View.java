package de.hetzge.sgame.common.newgeometry.views;

public interface IF_XY_View {

	public IF_Dimension_ImmutableView asDimensionImmutableView();

	public IF_Dimension_MutableView asDimensionMutableView();

	public IF_Position_ImmutableView asPositionImmutableView();

	public IF_Position_MutableView asPositionMutableView();

	public IF_Coordinate_ImmutableView asCoordinateImmutableView();

	public IF_Coordinate_MutableView asCoordinateMutableView();

}
