package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_MutableView;

public class Rectangle implements IF_Rectangle {

	private float x;
	private float y;

	private float width;
	private float height;

	@Override
	public void setPosition(IF_Position_ImmutableView position) {
		this.x = position.getFX();
		this.y = position.getFY();
	}

	@Override
	public void setCenteredPosition(IF_Position_ImmutableView position) {
		this.setPosition(position.copy().substract(this.getHalfDimension().asPositionImmutableView()));
	}

	@Override
	public void setDimension(IF_Dimension_ImmutableView dimension) {
		this.width = dimension.getWidth();
		this.height = dimension.getHeight();
	}

	@Override
	public IF_Position_ImmutableView getPosition() {
		return new XY(this.x, this.y);
	}

	@Override
	public IF_Position_ImmutableView getCenteredPosition() {
		return new XY(this.getPosition().copy().add(this.getHalfDimension()));
	}

	@Override
	public IF_Dimension_ImmutableView getDimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Position_ImmutableView getPositionA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Position_ImmutableView getPositionB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Position_ImmutableView getPositionC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Position_ImmutableView getPositionD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Rectangle_ImmutableView asRectangleImmutableView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IF_Rectangle_MutableView asRectangleMutableView() {
		// TODO Auto-generated method stub
		return null;
	}

}
