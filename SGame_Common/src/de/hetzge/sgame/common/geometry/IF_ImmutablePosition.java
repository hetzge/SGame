package de.hetzge.sgame.common.geometry;

public interface IF_ImmutablePosition<POSITION extends IF_Position> {

	public float getX();

	public float getY();

	public POSITION copy();

	public default IF_ImmutablePosition<POSITION> immutable() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public default POSITION mutable() {
		return (POSITION) this;
	}
}
