package de.hetzge.sgame.common.geometry;

public class InterpolateRectangle implements IF_Rectangle<InterpolatePosition, Dimension>, IF_SetupPositionInterpolate {

	private InterpolatePosition interpolatePosition;
	private Dimension dimension;

	private InterpolatePosition startPosition;
	private InterpolatePosition endPosition;
	private InterpolatePosition leftBottomNode;
	private InterpolatePosition rightTopNode;

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
	public IF_ImmutableDimension<Dimension> getDimension() {
		return this.dimension;
	}

	@Override
	public synchronized IF_ImmutablePosition<InterpolatePosition> getStartPosition() {
		if (this.startPosition == null) {
			Position startValue = this.interpolatePosition.getStartValue().copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			Position endValue = this.interpolatePosition.getEndValue().copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			this.startPosition = new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
		}
		return this.startPosition;
	}

	@Override
	public synchronized IF_ImmutablePosition<InterpolatePosition> getEndPosition() {
		if (this.endPosition == null) {
			Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			this.endPosition = new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
		}
		return this.endPosition;
	}

	@Override
	public synchronized IF_ImmutablePosition<InterpolatePosition> getLeftBottomNode() {
		if (this.leftBottomNode == null) {
			Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(-this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(-this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
			this.leftBottomNode = new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
		}
		return this.leftBottomNode;
	}

	@Override
	public synchronized IF_ImmutablePosition<InterpolatePosition> getRightTopNode() {
		if (this.rightTopNode == null) {
			Position startValue = this.interpolatePosition.getStartValue().copy().add(new Position(this.dimension.calculateHalfWidth(), -this.dimension.calculateHalfHeight()));
			Position endValue = this.interpolatePosition.getEndValue().copy().add(new Position(this.dimension.calculateHalfWidth(), -this.dimension.calculateHalfHeight()));
			this.rightTopNode = new InterpolatePosition(startValue, endValue, this.interpolatePosition.getStartTime(), this.interpolatePosition.getEndTime());
		}
		return this.rightTopNode;
	}

	@Override
	public synchronized void setPosition(InterpolatePosition position) {
		this.interpolatePosition = position;
		this.resetCache();
	}

	@Override
	public synchronized void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.resetCache();
	}

	@Override
	public synchronized void set(Position startValue, Position endValue, long timeSpanInMs) {
		this.interpolatePosition.set(startValue, endValue, timeSpanInMs);
		this.resetCache();
	}

	@Override
	public synchronized void set(Position endValue, long timeSpanInMs) {
		this.interpolatePosition.set(endValue, timeSpanInMs);
		this.resetCache();
	}

	@Override
	public synchronized void set(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs) {
		this.interpolatePosition.set(startValue, endValue, startTimeInMs, endTimeInMs);
		this.resetCache();
	}

	private void resetCache() {
		this.endPosition = null;
		this.startPosition = null;
		this.leftBottomNode = null;
		this.rightTopNode = null;
	}

}
