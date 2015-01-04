package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Rectangle implements Serializable, IF_ImmutableRectangle {

	/**
	 * The centered position of the rectangle.
	 */
	private IF_ImmutablePosition position;
	private IF_ImmutableDimension dimension;

	private IF_ImmutablePosition startPosition;
	private IF_ImmutablePosition endPosition;

	private IF_ImmutablePosition rightTopNode;
	private IF_ImmutablePosition leftBottomNode;

	public Rectangle(IF_ImmutablePosition position, IF_ImmutableDimension dimension) {
		this.position = position;
		this.dimension = dimension;
		this.recalculateRectangle();
	}

	public Rectangle() {
		this(new Position(), new Dimension());
	}

	@Override
	public void recalculateRectangle() {
		this.startPosition = this.position.copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		this.endPosition = this.startPosition.copy().add(new Position(this.dimension.getWidth(), this.dimension.getHeight()));
		this.rightTopNode = this.startPosition.copy().add(new Position(this.dimension.getWidth(), 0));
		this.leftBottomNode = this.startPosition.copy().add(new Position(0, this.dimension.getHeight()));
	}

	@Override
	public boolean doesOverlapWith(IF_ImmutableRectangle otherRectangle) {
		return Rectangle.doesOverlapWith(this, otherRectangle) || Rectangle.doesOverlapWith(otherRectangle, this);
	}

	private static boolean doesOverlapWith(IF_ImmutableRectangle rectangle, IF_ImmutableRectangle otherRectangle) {
		for (IF_ImmutablePosition position : new IF_ImmutablePosition[] { otherRectangle.getStartPosition(), otherRectangle.getEndPosition(), otherRectangle.getLeftBottomNode(),
				otherRectangle.getRightTopNode() }) {
			if (rectangle.doesOverlapWith(position))
				return true;
		}
		return false;
	}

	@Override
	public boolean doesOverlapWith(IF_ImmutablePosition position) {
		return position.getX() > this.startPosition.getX() && position.getX() < this.endPosition.getX() && position.getY() > this.startPosition.getY() && position.getY() < this.endPosition.getY();
	}

	@Override
	public IF_ImmutablePosition getPosition() {
		return this.position;
	}

	@Override
	public IF_ImmutableDimension getDimension() {
		return this.dimension;
	}

	@Override
	public IF_ImmutablePosition getStartPosition() {
		return this.startPosition;
	}

	@Override
	public IF_ImmutablePosition getEndPosition() {
		return this.endPosition;
	}

	@Override
	public IF_ImmutablePosition getLeftBottomNode() {
		return this.leftBottomNode;
	}

	@Override
	public IF_ImmutablePosition getRightTopNode() {
		return this.rightTopNode;
	}

	public void set(Rectangle rectangle) {
		this.position = rectangle.getPosition();
		this.dimension = rectangle.getDimension();
		this.recalculateRectangle();
	}

	public void setPosition(Position position) {
		this.position = position;
		this.recalculateRectangle();
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.recalculateRectangle();
	}

	@Override
	public String toString() {
		return this.startPosition + "/" + this.rightTopNode + "/" + this.leftBottomNode + "/" + this.endPosition;
	}

}
