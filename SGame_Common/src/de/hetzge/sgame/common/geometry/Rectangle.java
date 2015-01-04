package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Rectangle implements Serializable {

	/**
	 * The centered position of the rectangle.
	 */
	private Position position;
	private Dimension dimension;

	private Position startPosition;
	private Position endPosition;

	private Position rightTopNode;
	private Position leftBottomNode;

	public Rectangle(Position position, Dimension dimension) {
		this.position = position;
		this.dimension = dimension;
		this.recalculateRectangle();
	}

	public Rectangle() {
		this(new Position(), new Dimension());
	}

	public void recalculateRectangle() {
		this.startPosition = this.position.copy().subtract(new Position(this.dimension.calculateHalfWidth(), this.dimension.calculateHalfHeight()));
		this.endPosition = this.startPosition.copy().add(new Position(this.dimension.getWidth(), this.dimension.getHeight()));
		this.rightTopNode = this.startPosition.copy().add(new Position(this.dimension.getWidth(), 0));
		this.leftBottomNode = this.startPosition.copy().add(new Position(0, this.dimension.getHeight()));
	}

	public boolean doesOverlapWith(Rectangle otherRectangle) {
		return Rectangle.doesOverlapWith(this, otherRectangle) || Rectangle.doesOverlapWith(otherRectangle, this);
	}

	private static boolean doesOverlapWith(Rectangle rectangle, Rectangle otherRectangle) {
		for (Position position : new Position[] { otherRectangle.startPosition, otherRectangle.endPosition, otherRectangle.leftBottomNode, otherRectangle.rightTopNode }) {
			if (rectangle.doesOverlapWith(position))
				return true;
		}
		return false;
	}

	public boolean doesOverlapWith(Position position) {
		return position.getX() > this.startPosition.getX() && position.getX() < this.endPosition.getX() && position.getY() > this.startPosition.getY() && position.getY() < this.endPosition.getY();
	}

	public void set(Rectangle rectangle) {
		this.position = rectangle.getPosition();
		this.dimension = rectangle.getDimension();
		this.recalculateRectangle();
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
		this.recalculateRectangle();
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.recalculateRectangle();
	}

	public Position getStartPosition() {
		return this.startPosition;
	}

	public Position getEndPosition() {
		return this.endPosition;
	}

	@Override
	public String toString() {
		return this.startPosition + "/" + this.rightTopNode + "/" + this.leftBottomNode + "/" + this.endPosition;
	}

}