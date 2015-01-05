package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public interface IF_ImmutableRectangle<POSITION extends IF_Position, DIMENSION extends IF_Dimension> extends Serializable {

	public IF_ImmutablePosition<POSITION> getPosition();

	public IF_ImmutableDimension<DIMENSION> getDimension();

	public IF_ImmutablePosition<POSITION> getStartPosition();

	public IF_ImmutablePosition<POSITION> getEndPosition();

	public IF_ImmutablePosition<POSITION> getLeftBottomNode();

	public IF_ImmutablePosition<POSITION> getRightTopNode();

	public default IF_ImmutableRectangle<POSITION, DIMENSION> immutable() {
		return this;
	}

	public default IF_Rectangle<POSITION, DIMENSION> mutable() {
		return (IF_Rectangle<POSITION, DIMENSION>) this;
	}

	public default boolean doesOverlapWith(IF_ImmutableRectangle<POSITION, DIMENSION> otherRectangle) {
		return this.doesOverlapWith(this, otherRectangle) || this.doesOverlapWith(otherRectangle, this);
	}

	default boolean doesOverlapWith(IF_ImmutableRectangle<POSITION, DIMENSION> rectangle, IF_ImmutableRectangle<POSITION, DIMENSION> otherRectangle) {
		for (IF_ImmutablePosition<POSITION> position : new IF_ImmutablePosition[] { otherRectangle.getStartPosition(), otherRectangle.getEndPosition(), otherRectangle.getLeftBottomNode(),
				otherRectangle.getRightTopNode() }) {
			if (rectangle.doesOverlapWith(position))
				return true;
		}
		return false;
	}

	public default boolean doesOverlapWith(IF_ImmutablePosition<POSITION> position) {
		return position.getX() > this.getStartPosition().getX() && position.getX() < this.getEndPosition().getX() && position.getY() > this.getStartPosition().getY()
				&& position.getY() < this.getEndPosition().getY();
	}

}
