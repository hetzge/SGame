package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_MutableView;

public class InterpolateRectangle implements IF_Rectangle_MutableView {

	private float startX;
	private float startY;

	private float endX;
	private float endY;

	private float startWidth;
	private float startHeight;

	private float endWidth;
	private float endHeight;

	private long startTimeInMs;
	private long endTimeInMs;

	public InterpolateRectangle() {
		this(new Rectangle(), 0, new Rectangle(), 0);
	}

	public InterpolateRectangle(IF_Rectangle start, IF_Rectangle end, long timeSpanInMs) {
		this(start, System.currentTimeMillis(), end, System.currentTimeMillis() + timeSpanInMs);
	}

	public InterpolateRectangle(IF_Rectangle start, long startTimeInMs, IF_Rectangle end, long endTimeInMs) {
		this.startTimeInMs = startTimeInMs;
		this.endTimeInMs = endTimeInMs;

		this.startX = start.getCenteredPosition().getFX();
		this.startY = start.getCenteredPosition().getFY();

		this.endX = end.getCenteredPosition().getFX();
		this.endY = end.getCenteredPosition().getFY();

		this.startWidth = start.getDimension().getWidth();
		this.startHeight = start.getDimension().getHeight();

		this.endWidth = end.getDimension().getWidth();
		this.endHeight = end.getDimension().getHeight();
	}

	public IF_Position getInterpolatedCenteredPosition() {
		return new XY(Util.interpolateFloat(this.startX, this.startTimeInMs, this.endX, this.endTimeInMs), Util.interpolateFloat(this.startY, this.startTimeInMs, this.endY, this.endTimeInMs));
	}

	public IF_Dimension getInterpolatedDimension() {
		return new XY(Util.interpolateFloat(this.startWidth, this.startTimeInMs, this.endWidth, this.endTimeInMs), Util.interpolateFloat(this.startHeight, this.startTimeInMs, this.endHeight, this.endTimeInMs));
	}

	public void setCenteredPosition(IF_Position start, long startTimeInMs, IF_Position end, long endTimeInMs) {
		this.startX = start.getFX();
		this.startY = start.getFY();
		this.endX = end.getFX();
		this.endY = end.getFY();

		this.startTimeInMs = startTimeInMs;
		this.endTimeInMs = endTimeInMs;
	}

	@Override
	public void setCenteredPosition(IF_Position_ImmutableView position) {
		this.startX = position.getFX();
		this.startY = position.getFY();
		this.endX = position.getFX();
		this.endY = position.getFY();

		long currentTimeMillis = System.currentTimeMillis();
		this.startTimeInMs = currentTimeMillis;
		this.endTimeInMs = currentTimeMillis;
	}

	public void setCenteredPosition(IF_Position_ImmutableView goal, long timeSpanInMs){
		IF_Position interpolatedCenteredPosition = this.getInterpolatedCenteredPosition();
		this.startX = interpolatedCenteredPosition.getFX();
		this.startY = interpolatedCenteredPosition.getFY();
		this.endX = goal.getFX();
		this.endY = goal.getFY();

		long currentTimeMillis = System.currentTimeMillis();
		this.startTimeInMs = currentTimeMillis;
		this.endTimeInMs = currentTimeMillis + timeSpanInMs;
	}

	public void setDimension(IF_Dimension start, IF_Dimension end, long timeSpanInMs) {
		this.startWidth = start.getWidth();
		this.startHeight = start.getHeight();
		this.endWidth = end.getWidth();
		this.endHeight = end.getHeight();

		long currentTimeMillis = System.currentTimeMillis();
		this.startTimeInMs = currentTimeMillis;
		this.endTimeInMs = currentTimeMillis + timeSpanInMs;
	}

	@Override
	public void setDimension(IF_Dimension_ImmutableView dimension) {
		this.startWidth = dimension.getWidth();
		this.startHeight = dimension.getHeight();
		this.endWidth = dimension.getWidth();
		this.endHeight = dimension.getHeight();

		long currentTimeMillis = System.currentTimeMillis();
		this.startTimeInMs = currentTimeMillis;
		this.endTimeInMs = currentTimeMillis;
	}

	@Override
	public IF_Rectangle_ImmutableView asRectangleImmutableView() {
		return this;
	}

	@Override
	public IF_Rectangle_MutableView asRectangleMutableView() {
		return this;
	}

	@Override
	public void setPositionA(IF_Position_ImmutableView position) {
		this.setCenteredPosition(position.copy().add(this.getHalfDimension().asPositionImmutableView()));
	}

	@Override
	public IF_Position_ImmutableView getCenteredPosition() {
		return new XY(this.getInterpolatedCenteredPosition());
	}

	@Override
	public IF_Dimension_ImmutableView getDimension() {
		return new XY(this.getInterpolatedDimension());
	}

}
