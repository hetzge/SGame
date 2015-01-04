package de.hetzge.sgame.common.geometry;

public interface IF_ImmutableDimension {

	public float getWidth();

	public float getHeight();

	public float calculateHalfWidth();

	public float calculateHalfHeight();

	public default IF_ImmutableDimension immutable() {
		return this;
	}

	public default Dimension mutable() {
		return (Dimension) this;
	}

}
