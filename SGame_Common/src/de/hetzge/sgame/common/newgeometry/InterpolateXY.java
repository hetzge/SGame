package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.Util;

public class InterpolateXY extends XY {

	private float endX;
	private float endY;

	private long startTimeInMs;
	private long endTimeInMs;

	public InterpolateXY() {
		this(new XY(0f), 0L, new XY(0f), 0L);
	}

	public InterpolateXY(IF_XY value) {
		this(value, System.currentTimeMillis(), value, System.currentTimeMillis());
	}

	public InterpolateXY(IF_XY start, IF_XY end, long timeSpanInMs) {
		this(start, System.currentTimeMillis(), end, System.currentTimeMillis() + timeSpanInMs);
	}

	public InterpolateXY(IF_XY start, long startTimeInMs, IF_XY end, long endTimeInMs) {
		super(start.getX(), start.getY());
		this.set(start, startTimeInMs, end, endTimeInMs);
	}

	public void set(IF_XY start, long startTimeInMs, IF_XY end, long endTimeInMs) {
		this.x = start.getX();
		this.y = start.getY();

		this.startTimeInMs = startTimeInMs;
		this.endTimeInMs = endTimeInMs;

		this.endX = end.getX();
		this.endY = end.getY();
	}

	public void set(IF_XY start, IF_XY end, long timeSpanInMs) {
		this.set(start, System.currentTimeMillis(), end, System.currentTimeMillis() + timeSpanInMs);
	}

	public void set(IF_XY end, long timeSpanInMs) {
		this.set(this, System.currentTimeMillis(), end, System.currentTimeMillis() + timeSpanInMs);
	}

	public void set(IF_XY position){
		this.set(position, position, 0);
	}

	public void stop(){
		this.set(this, 0);
	}

	@Override
	public float getX() {
		return Util.interpolateFloat(this.x, this.startTimeInMs, this.endX, this.endTimeInMs);
	}

	@Override
	public float getY() {
		return Util.interpolateFloat(this.y, this.startTimeInMs, this.endY, this.endTimeInMs);
	}

	@Override
	public <T extends IF_XY> T setX(float x) {
		this.x = x;
		this.endX = x;
		this.startTimeInMs = System.currentTimeMillis();
		this.endTimeInMs = System.currentTimeMillis();
		return (T) this;
	}

	@Override
	public <T extends IF_XY> T setY(float y) {
		this.y = y;
		this.endY = y;
		this.startTimeInMs = System.currentTimeMillis();
		this.endTimeInMs = System.currentTimeMillis();
		return (T) this;
	}

}
