package de.hetzge.sgame.common.newgeometry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;

public interface IF_Rectangle extends Serializable{

	// TODO getAx() ... optimieren 
	IF_Position_ImmutableView getPositionA();

	IF_Position_ImmutableView getPositionB();

	IF_Position_ImmutableView getPositionC();

	IF_Position_ImmutableView getPositionD();

	IF_Position_ImmutableView getCenteredPosition();

	IF_Dimension_ImmutableView getDimension();

	public default boolean doesOverlapWith(IF_Rectangle otherRectangle) {
		return this.doesOverlapWith(this, otherRectangle) || this.doesOverlapWith(otherRectangle, this);
	}

	public default boolean doesOverlapWith(IF_Rectangle rectangle, IF_Rectangle otherRectangle) {
		IF_Position_ImmutableView positionA = otherRectangle.getPositionA();
		if (rectangle.doesOverlapWith(positionA.getFX(), positionA.getFY())) {
			return true;
		}
		IF_Position_ImmutableView positionB = otherRectangle.getPositionB();
		if (rectangle.doesOverlapWith(positionB.getFX(), positionB.getFY())) {
			return true;
		}
		IF_Position_ImmutableView positionC = otherRectangle.getPositionC();
		if (rectangle.doesOverlapWith(positionC.getFX(), positionC.getFY())) {
			return true;
		}
		IF_Position_ImmutableView positionD = otherRectangle.getPositionD();
		if (rectangle.doesOverlapWith(positionD.getFX(), positionD.getFY())) {
			return true;
		}
		return false;
	}

	public default boolean doesOverlapWith(float x, float y) {
		IF_Position_ImmutableView positionA = this.getPositionA();
		IF_Position_ImmutableView positionD = this.getPositionD();
		return x > positionA.getFX() && x < positionD.getFX() && y > positionA.getFY() && y < positionD.getFY();
	}

	public default <T extends IF_Rectangle> T copy() {
		try {
			return (T) getClass().getConstructor(Float.class, Float.class, Float.class, Float.class).newInstance(this.getCenteredPosition().getX(), this.getCenteredPosition().getY(), this.getDimension().getWidth(), this.getDimension().getHeight());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException("Every implementation of IF_Rectangle must have a constructor with float x, float y, float width and float height as parameter.");
		}
	}

}
