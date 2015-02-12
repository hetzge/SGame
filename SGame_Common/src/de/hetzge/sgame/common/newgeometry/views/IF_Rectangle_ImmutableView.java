package de.hetzge.sgame.common.newgeometry.views;

import de.hetzge.sgame.common.newgeometry.XY;

public interface IF_Rectangle_ImmutableView {

	IF_Position_ImmutableView getPosition();

	IF_Position_ImmutableView getCenteredPosition();

	IF_Dimension_ImmutableView getDimension();

	default IF_Dimension_ImmutableView getHalfDimension() {
		XY copy = new XY(this.getDimension());
		XY halfDimension = copy.divide(new XY(2, 2));
		return halfDimension;
	}

	IF_Position_ImmutableView getPositionA();

	IF_Position_ImmutableView getPositionB();

	IF_Position_ImmutableView getPositionC();

	IF_Position_ImmutableView getPositionD();

}
