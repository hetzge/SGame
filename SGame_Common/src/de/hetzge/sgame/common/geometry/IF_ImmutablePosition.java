package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public interface IF_ImmutablePosition<POSITION extends IF_Position> extends Serializable {

	public float getX();

	public float getY();

	public POSITION copy();

	public float distance(IF_ImmutablePosition<?> otherPosition);

	public default IF_ImmutablePosition<POSITION> immutable() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public default POSITION mutable() {
		return (POSITION) this;
	}
}
