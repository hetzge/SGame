package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public interface IF_ImmutableDimension<DIMENSION extends IF_Dimension<?>> extends Serializable {

	public float getWidth();

	public float getHeight();

	public float calculateHalfWidth();

	public float calculateHalfHeight();

	public default IF_ImmutableDimension immutable() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public default DIMENSION mutable() {
		return (DIMENSION) this;
	}

}
