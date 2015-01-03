package de.hetzge.sgame.common;

import java.util.EnumSet;

import de.hetzge.sgame.common.geometry.Position;

public enum Orientation {

	NORTH(new Position(0, -1)), NORTH_EAST(new Position(1, -1)), EAST(new Position(1, 0)), SOUTH_EAST(new Position(1, 1)), SOUTH(new Position(0, 1)), SOUTH_WEST(new Position(-1, 1)), WEST(
			new Position(-1, 0)), NORTH_WEST(new Position(-1, -1));

	public static final EnumSet<Orientation> Simple = EnumSet.of(NORTH, EAST, SOUTH, WEST);

	public static final Orientation DEFAULT = SOUTH;

	public final Position orientationFactor;

	/**
	 * Holds a optimized orientation factor for diagonal movement.
	 */
	public final Position orientationFactorOptimized;

	private Orientation(Position orientationFactor) {
		this.orientationFactor = orientationFactor;
		if (orientationFactor.getX() != 0 && orientationFactor.getY() != 0) {
			this.orientationFactorOptimized = new Position((float) (1 / Math.sqrt(2d) * orientationFactor.getX()), (float) (1 / Math.sqrt(2d) * orientationFactor.getY()));
		} else {
			this.orientationFactorOptimized = orientationFactor;
		}
	}
}
