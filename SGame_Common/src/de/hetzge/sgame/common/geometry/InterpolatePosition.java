package de.hetzge.sgame.common.geometry;

import de.hetzge.sgame.common.InterpolateFloat;

public class InterpolatePosition implements IF_Position<InterpolatePosition>, IF_SetupPositionInterpolate {

	private InterpolateFloat positionXInterpolate;
	private InterpolateFloat positionYInterpolate;

	private Position startValue;
	private Position endValue;
	private long startTime = 0;
	private long endTime = 0;

	public InterpolatePosition() {
		this(new InterpolateFloat(0f, 0L, 0f, 0L), new InterpolateFloat(0f, 0L, 0f, 0L));
	}

	private InterpolatePosition(InterpolateFloat positionXInterpolate, InterpolateFloat positionYInterpolate) {
		this.positionXInterpolate = positionXInterpolate;
		this.positionYInterpolate = positionYInterpolate;
		this.startValue = new Position(positionXInterpolate.getStartValue(), positionYInterpolate.getStartValue());
		this.endValue = new Position(positionXInterpolate.getEndValue(), positionYInterpolate.getEndValue());
	}

	public InterpolatePosition(Position startValue, Position endValue, long timeSpanInMs) {
		this.set(startValue, endValue, timeSpanInMs);
	}

	public InterpolatePosition(Position endValue, long timeSpanInMs) {
		this.set(endValue, timeSpanInMs);
	}

	public InterpolatePosition(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs) {
		this.set(startValue, endValue, startTimeInMs, endTimeInMs);
	}

	@Override
	public void set(Position startValue, Position endValue, long timeSpanInMs) {
		this.set(startValue, endValue, System.currentTimeMillis(), System.currentTimeMillis() + timeSpanInMs);
	}

	@Override
	public void set(Position endValue, long timeSpanInMs) {
		this.set(this.startValue, endValue, System.currentTimeMillis(), System.currentTimeMillis() + timeSpanInMs);
	}

	@Override
	public void set(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs) {
		this.positionXInterpolate = new InterpolateFloat(startValue.getX(), startTimeInMs, endValue.getX(), endTimeInMs);
		this.positionYInterpolate = new InterpolateFloat(startValue.getY(), startTimeInMs, endValue.getY(), endTimeInMs);
		this.startValue = startValue;
		this.endValue = endValue;
	}

	@Override
	public float getX() {
		return this.positionXInterpolate.calculateCurrentNumber();
	}

	@Override
	public float getY() {
		return this.positionYInterpolate.calculateCurrentNumber();
	}

	public IF_ImmutablePosition<Position> getStartValue() {
		return this.startValue;
	}

	public IF_ImmutablePosition<Position> getEndValue() {
		return this.endValue;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return this.endTime;
	}

	@Override
	public InterpolatePosition copy() {
		return new InterpolatePosition(new InterpolateFloat(this.positionXInterpolate), new InterpolateFloat(this.positionYInterpolate));
	}

}
