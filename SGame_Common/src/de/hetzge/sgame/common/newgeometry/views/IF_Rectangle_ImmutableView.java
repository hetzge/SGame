package de.hetzge.sgame.common.newgeometry.views;

import de.hetzge.sgame.common.newgeometry.IF_Rectangle;
import de.hetzge.sgame.common.newgeometry.XY;

public interface IF_Rectangle_ImmutableView extends IF_Rectangle {

	@Override
	public IF_Position_ImmutableView getCenteredPosition();

	@Override
	public IF_Dimension_ImmutableView getDimension();

	public default IF_Dimension_ImmutableView getHalfDimension() {
		XY copy = new XY(this.getDimension());
		XY halfDimension = copy.divide(new XY(2, 2));
		return halfDimension;
	}

	@Override
	public default IF_Position_ImmutableView getPositionA() {
		return this.getCenteredPosition().copy().substract(this.getHalfDimension().asPositionImmutableView());
	}

	@Override
	public default IF_Position_ImmutableView getPositionB() {
		IF_Dimension_ImmutableView halfDimension = this.getHalfDimension();
		return this.getCenteredPosition().copy().add(new XY(halfDimension.getWidth(), -halfDimension.getHeight()));
	}

	@Override
	public default IF_Position_ImmutableView getPositionC() {
		IF_Dimension_ImmutableView halfDimension = this.getHalfDimension();
		return this.getCenteredPosition().copy().add(new XY(-halfDimension.getWidth(), halfDimension.getHeight()));
	}

	@Override
	public default IF_Position_ImmutableView getPositionD() {
		return this.getCenteredPosition().copy().add(this.getHalfDimension().asPositionImmutableView());
	}

}
