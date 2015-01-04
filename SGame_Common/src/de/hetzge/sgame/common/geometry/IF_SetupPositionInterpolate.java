package de.hetzge.sgame.common.geometry;

public interface IF_SetupPositionInterpolate {

	public abstract void set(Position startValue, Position endValue, long timeSpanInMs);

	public abstract void set(Position endValue, long timeSpanInMs);

	public abstract void set(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs);

}