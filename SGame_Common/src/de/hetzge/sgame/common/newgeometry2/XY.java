package de.hetzge.sgame.common.newgeometry2;

public class XY implements IF_Coordinate_Mutable, IF_Position_Mutable, IF_Dimension_Mutable {
	private float x;
	private float y;

	public XY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public XY(float value) {
		this(value, value);
	}

	public XY() {
		this(0f, 0f);
	}

	public static XY xy(float x, float y) {
		return new XY(x, y);
	}

	public static XY xy(float value) {
		return new XY(value);
	}

	public static XY xy() {
		return new XY();
	}

	void _setX(float x) {
		this.x = x;
	}

	void _setY(float y) {
		this.y = y;
	}

	float _getX() {
		return this.x;
	}

	float _getY() {
		return this.y;
	}

	@Override
	public XY add(IF_XY xy) {
		this.getXY()._setX(this.getXY()._getX() + xy.getXY()._getX());
		this.getXY()._setY(this.getXY()._getY() + xy.getXY()._getY());
		return this;
	}

	@Override
	public XY minus(IF_XY xy) {
		this.getXY()._setX(this.getXY()._getX() - xy.getXY()._getX());
		this.getXY()._setY(this.getXY()._getY() - xy.getXY()._getY());
		return this;
	}

	@Override
	public XY divide(IF_XY xy) {
		this.getXY()._setX(this.getXY()._getX() / xy.getXY()._getX());
		this.getXY()._setY(this.getXY()._getY() / xy.getXY()._getY());
		return this;
	}

	@Override
	public XY multiply(IF_XY xy) {
		this.getXY()._setX(this.getXY()._getX() * xy.getXY()._getX());
		this.getXY()._setY(this.getXY()._getY() * xy.getXY()._getY());
		return this;
	}

	@Override
	public XY set(IF_XY xy) {
		this.getXY()._setX(xy.getXY()._getX());
		this.getXY()._setY(xy.getXY()._getY());
		return this;
	}

	@Override
	public XY abs() {
		this.setFX(Math.abs(this.getFX()));
		this.setFY(Math.abs(this.getFY()));
		return this;
	}

	@Override
	public XY getXY() {
		return this;
	}

	@Override
	public String toString() {
		return "XY [x=" + this.x + ", y=" + this.y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x);
		result = prime * result + Float.floatToIntBits(this.y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		XY other = (XY) obj;
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

}
