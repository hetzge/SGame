package de.hetzge.sgame.common;

public class InterpolateDouble {

	private final double startValue;
	private final long startTimeInMs;

	private final double endValue;
	private final long endTimeInMs;

	public InterpolateDouble(double startValue, long startTimeInMs, double endValue, long endTimeInMs) {
		this.startValue = startValue;
		this.startTimeInMs = startTimeInMs;
		this.endValue = endValue;
		this.endTimeInMs = endTimeInMs;
	}

	public double calculateCurrentNumber() {
		long currentMs = System.currentTimeMillis();
		long interpolationTimeSpan = this.endTimeInMs - this.startTimeInMs;
		long interpolationTimeSpanDoneInPercent = currentMs / interpolationTimeSpan;
		double valueSpan = this.endValue - this.startValue;
		return valueSpan * interpolationTimeSpanDoneInPercent;
	}
}
