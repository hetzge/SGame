package de.hetzge.sgame.common.geometry;

public class InterpolateRectangle implements IF_Rectangle<InterpolatePosition, Dimension>, IF_SetupPositionInterpolate {

	private InterpolatePosition interpolatePosition;
	private Dimension dimension;

	public InterpolateRectangle() {
		this(new InterpolatePosition(), new Dimension());
	}

	public InterpolateRectangle(InterpolatePosition interpolatePosition, Dimension dimension) {
		this.interpolatePosition = interpolatePosition;
		this.dimension = dimension;
	}

	@Override
	public InterpolatePosition getPosition() {
		return this.interpolatePosition;
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}

	// TODO Optimierung (viele Objekterzeugungen)
	@Override
	public InterpolatePosition getStartPosition() {
		Position startValue = this.interpolatePosition.getStartValue().copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		Position endValue = this.interpolatePosition.getEndValue().copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		return new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
	}

	@Override
	public InterpolatePosition getEndPosition() {
		Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		return new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
	}

	@Override
	public InterpolatePosition getLeftBottomNode() {
		Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(-this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(-this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		return new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
	}

	@Override
	public InterpolatePosition getRightTopNode() {
		Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(this.dimension.calculateHalfWidth(), -this.dimension.calculateHalfHeight()));
		Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(this.dimension.calculateHalfWidth(), -this.dimension.calculateHalfHeight()));
		return new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
	}

	@Override
	public void setPosition(InterpolatePosition position) {
		this.interpolatePosition = position;
	}

	@Override
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	@Override
	public void set(Position startValue, Position endValue, long timeSpanInMs) {
		this.interpolatePosition.set(startValue, endValue, timeSpanInMs);
	}

	@Override
	public void set(Position endValue, long timeSpanInMs) {
		this.interpolatePosition.set(endValue, timeSpanInMs);
	}

	@Override
	public void set(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs) {
		this.interpolatePosition.set(startValue, endValue, startTimeInMs, endTimeInMs);
	}

}
