package de.hetzge.sgame.map;

public class TileLayer {

	private final IF_Ground ground;
	private final TileOrientation tileOrientation;
	private boolean collision = false;

	public TileLayer(IF_Ground ground, TileOrientation tileOrientation) {
		this(ground, tileOrientation, false);
	}

	public TileLayer(IF_Ground ground, TileOrientation tileOrientation, boolean collision) {
		this.ground = ground;
		this.tileOrientation = tileOrientation;
		this.collision = collision;
	}

	public IF_Ground getGround() {
		return this.ground;
	}

	public TileOrientation getTileOrientation() {
		return this.tileOrientation;
	}

	public boolean isCollision() {
		return this.collision;
	}

	String calculateKey() {
		return this.ground.toString() + "-" + this.tileOrientation.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ground == null) ? 0 : this.ground.hashCode());
		result = prime * result + ((this.tileOrientation == null) ? 0 : this.tileOrientation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		TileLayer other = (TileLayer) obj;
		if (this.ground == null) {
			if (other.ground != null)
				return false;
		} else if (!this.ground.equals(other.ground))
			return false;
		if (this.tileOrientation != other.tileOrientation)
			return false;
		return true;
	}

}