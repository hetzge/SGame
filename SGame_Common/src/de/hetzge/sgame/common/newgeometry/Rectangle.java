package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_MutableView;

public class Rectangle implements IF_Rectangle_MutableView {

	private float x;
	private float y;

	private float width;
	private float height;

	public Rectangle() {
		this(0f, 0f, 0f, 0f);
	}

	public Rectangle(IF_Position_ImmutableView position, IF_Dimension_ImmutableView dimension) {
		this(position.getFX(), position.getFY(), dimension.getWidth(), dimension.getHeight());
	}

	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void setPositionA(IF_Position_ImmutableView position) {
		this.x = position.getFX();
		this.y = position.getFY();
	}

	@Override
	public void setCenteredPosition(IF_Position_ImmutableView position) {
		this.setPositionA(position.copy().substract(this.getHalfDimension().asPositionImmutableView()));
	}

	@Override
	public void setDimension(IF_Dimension_ImmutableView dimension) {
		this.width = dimension.getWidth();
		this.height = dimension.getHeight();
	}

	@Override
	public IF_Position_ImmutableView getCenteredPosition() {
		return new XY(this.x, this.y);
	}

	@Override
	public IF_Dimension_ImmutableView getDimension() {
		return new XY(this.width, this.height);
	}


	@Override
	public IF_Rectangle_ImmutableView asRectangleImmutableView() {
		return this;
	}

	@Override
	public IF_Rectangle_MutableView asRectangleMutableView() {
		return this;
	}

}
