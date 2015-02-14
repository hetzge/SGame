package de.hetzge.sgame.common.newgeometry.views;

public interface IF_Rectangle_MutableView extends IF_Rectangle_ImmutableView {

	void setPositionA(IF_Position_ImmutableView position);

	void setDimension(IF_Dimension_ImmutableView dimension);

	void setCenteredPosition(IF_Position_ImmutableView position);

	@Override
	public default IF_Rectangle_ImmutableView asRectangleImmutableView() {
		return this;
	}

	@Override
	public default IF_Rectangle_MutableView asRectangleMutableView() {
		return this;
	}

}
