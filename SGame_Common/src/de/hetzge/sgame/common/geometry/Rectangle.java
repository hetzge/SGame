package de.hetzge.sgame.common.geometry;

import java.io.Serializable;

public class Rectangle implements Serializable, IF_Rectangle<Position, Dimension> {

	/**
	 * The centered position of the rectangle.
	 */
	protected Position position;
	protected Dimension dimension;

	protected Position startPosition;
	protected Position endPosition;

	protected Position rightTopNode;
	protected Position leftBottomNode;

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

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}

	@Override
	public Position getStartPosition() {
		return this.startPosition;
	}

	@Override
	public Position getEndPosition() {
		return this.endPosition;
	}

	@Override
	public Position getLeftBottomNode() {
		return this.leftBottomNode;
	}

	@Override
	public Position getRightTopNode() {
		return this.rightTopNode;
	}

	public void set(Rectangle rectangle) {
		this.position = rectangle.getPosition();
		this.dimension = rectangle.getDimension();
		this.recalculateRectangle();
	}

	@Override
	public void setPosition(Position position) {
		this.position = position;
		this.recalculateRectangle();
	}

	@Override
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.recalculateRectangle();
	}

	@Override
	public String toString() {
		return this.startPosition + "/" + this.rightTopNode + "/" + this.leftBottomNode + "/" + this.endPosition;
	}

}
