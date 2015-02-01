package de.hetzge.sgame.common.newgeometry;

public class XY implements IF_Position, IF_Dimension, IF_Coordinate, IF_XY<XY> {

	private float x;
	private float y;

	public XY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public XY create(IF_XY<?> xy) {
		return this.create(xy.getX(), xy.getY());
	}

	@Override
	public XY create(float x, float y) {
		return new XY(x, y);
	}

	@Override
	public XY copy() {
		return this.create(this.getX(), this.getY());
	}

}
