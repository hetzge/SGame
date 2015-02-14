package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.Util;

public class InterpolateXY implements IF_XY {

	private float startX;
	private float startY;

	private float endX;
	private float endY;

	private long startTimeInMs;
	private long endTimeInMs;

	public InterpolateXY() {
		this(new XY(0f), 0L, new XY(0f), 0L);
	}

	public InterpolateXY(IF_XY value){
		this(value, System.currentTimeMillis(), value, System.currentTimeMillis());
	}

	public InterpolateXY(IF_XY start, IF_XY end, long timeSpanInMs) {
		this(start, System.currentTimeMillis(), end, System.currentTimeMillis() + timeSpanInMs);
	}

	public InterpolateXY(IF_XY start, long startTimeInMs, IF_XY end, long endTimeInMs) {
		this.startTimeInMs = startTimeInMs;
		this.endTimeInMs = endTimeInMs;

		this.startX = start.getX();
		this.startY = start.getY();

		this.endX = end.getX();
		this.endY = end.getY();
	}

	public IF_XY getInterpolatedXY() {
		return new XY(this.getX(), this.getY());
	}

	@Override
	public float getX() {
		return Util.interpolateFloat(this.startX, this.startTimeInMs, this.endX, this.endTimeInMs);
	}

	@Override
	public float getY() {
		return Util.interpolateFloat(this.startY, this.startTimeInMs, this.endY, this.endTimeInMs);
	}

	@Override
	public <T extends IF_XY> T setX(float x) {
		this.startX = x;
		this.endX = x;
		this.startTimeInMs = System.currentTimeMillis();
		this.endTimeInMs = System.currentTimeMillis();
		return (T) this;
	}

	@Override
	public <T extends IF_XY> T setY(float y) {
		this.startY = y;
		this.endY = y;
		this.startTimeInMs = System.currentTimeMillis();
		this.endTimeInMs = System.currentTimeMillis();
		return (T) this;
	}

}
