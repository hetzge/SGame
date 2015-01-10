package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public interface IF_ImmutableComplexRectangle<POSITION extends IF_Position, DIMENSION extends IF_Dimension> extends Serializable, IF_ImmutablePrimitivRectangle {

	public IF_ImmutablePosition<POSITION> getPosition();

	public IF_ImmutableDimension<DIMENSION> getDimension();

	public IF_ImmutablePosition<POSITION> getStartPosition();

	public IF_ImmutablePosition<POSITION> getEndPosition();

	public IF_ImmutablePosition<POSITION> getLeftBottomNode();

	public IF_ImmutablePosition<POSITION> getRightTopNode();

	public default IF_ImmutableComplexRectangle<POSITION, DIMENSION> immutable() {
		return this;
	}

	public default IF_Rectangle<POSITION, DIMENSION> mutable() {
		return (IF_Rectangle<POSITION, DIMENSION>) this;
	}

}
