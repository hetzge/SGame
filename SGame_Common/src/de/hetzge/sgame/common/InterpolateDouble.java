package de.hetzge.sgame.common;

import java.io.Serializable;

public class InterpolateDouble implements Serializable {

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
		long interpolationTimeSpan = this.endTimeInMs - this.startTimeInMs;
		if (interpolationTimeSpan == 0)
			return this.endValue;
		long currentMs = System.currentTimeMillis() - this.startTimeInMs;
		double interpolationTimeSpanDoneInPercent = (double) currentMs / interpolationTimeSpan;
		if (interpolationTimeSpanDoneInPercent > 1f)
			return this.endValue;
		double valueSpan = this.endValue - this.startValue;
		return this.startValue + valueSpan * interpolationTimeSpanDoneInPercent;
	}
}
