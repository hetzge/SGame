package de.hetzge.sgame.common.geometry;

public interface IF_ImmutablePosition {

	public float getX();

	public float getY();

	public Position copy();

	public default IF_ImmutablePosition immutable() {
		return this;
	}

	public default Position mutable() {
		return (Position) this;
	}
}
