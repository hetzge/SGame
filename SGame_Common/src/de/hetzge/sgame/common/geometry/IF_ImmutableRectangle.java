package de.hetzge.sgame.common.geometry;

public interface IF_ImmutableRectangle {

	public void recalculateRectangle();

	public boolean doesOverlapWith(IF_ImmutableRectangle otherRectangle);

	public boolean doesOverlapWith(IF_ImmutablePosition position);

	public IF_ImmutablePosition getPosition();

	public IF_ImmutableDimension getDimension();

	public IF_ImmutablePosition getStartPosition();

	public IF_ImmutablePosition getEndPosition();

	public IF_ImmutablePosition getLeftBottomNode();

	public IF_ImmutablePosition getRightTopNode();

	public default IF_ImmutableRectangle immutable() {
		return this;
	}

	public default Rectangle mutable() {
		return (Rectangle) this;
	}

}
