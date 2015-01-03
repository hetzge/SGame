package de.hetzge.sgame.common;

public class TimeInterpolateDouble {

	private double startValue;
	private long startTimeInMs;

	private double endValue;
	private long endTimeInMs;

	public double calculateCurrentNumber() {
		long currentMs = System.currentTimeMillis();
		long interpolationTimeSpan = this.endTimeInMs - this.startTimeInMs;
		long interpolationTimeSpanDoneInPercent = currentMs / interpolationTimeSpan;
		double valueSpan = this.endValue - this.startValue;
		return valueSpan * interpolationTimeSpanDoneInPercent;
	}
}
