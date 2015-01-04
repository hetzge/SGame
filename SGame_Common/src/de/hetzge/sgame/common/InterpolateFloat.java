package de.hetzge.sgame.common;

import java.io.Serializable;

public class InterpolateFloat implements Serializable {
	private final float startValue;
	private final long startTimeInMs;

	private final float endValue;
	private final long endTimeInMs;

	public InterpolateFloat(float startValue, long startTimeInMs, float endValue, long endTimeInMs) {
		this.startValue = startValue;
		this.startTimeInMs = startTimeInMs;
		this.endValue = endValue;
		this.endTimeInMs = endTimeInMs;
	}

	public InterpolateFloat(InterpolateFloat interpolateFloat) {
		this.startValue = interpolateFloat.getStartValue();
		this.startTimeInMs = interpolateFloat.getStartTimeInMs();
		this.endValue = interpolateFloat.getEndValue();
		this.endTimeInMs = interpolateFloat.getEndTimeInMs();
	}

	public float calculateCurrentNumber() {
		long interpolationTimeSpan = this.endTimeInMs - this.startTimeInMs;
		if (interpolationTimeSpan == 0)
			return this.endValue;
		long currentMs = System.currentTimeMillis() - this.startTimeInMs;
		float interpolationTimeSpanDoneInPercent = (float) currentMs / interpolationTimeSpan;
		if (interpolationTimeSpanDoneInPercent > 1f)
			return this.endValue;
		float valueSpan = this.endValue - this.startValue;
		return this.startValue + valueSpan * interpolationTimeSpanDoneInPercent;
	}

	public float getStartValue() {
		return this.startValue;
	}

	public long getStartTimeInMs() {
		return this.startTimeInMs;
	}

	public float getEndValue() {
		return this.endValue;
	}

	public long getEndTimeInMs() {
		return this.endTimeInMs;
	}

}