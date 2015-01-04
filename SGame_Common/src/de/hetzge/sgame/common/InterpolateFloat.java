package de.hetzge.sgame.common;

public class InterpolateFloat {
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
		long currentMs = System.currentTimeMillis();
		long interpolationTimeSpan = this.endTimeInMs - this.startTimeInMs;
		long interpolationTimeSpanDoneInPercent = currentMs / interpolationTimeSpan;
		float valueSpan = this.endValue - this.startValue;
		return valueSpan * interpolationTimeSpanDoneInPercent;
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
