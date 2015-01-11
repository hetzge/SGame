package de.hetzge.sgame.common;

import java.util.EnumSet;

import javolution.util.FastMap;
import de.hetzge.sgame.common.geometry.Position;

public enum Orientation {

	// important that north, east, south and west are first
	NORTH(new Position(0, -1)), EAST(new Position(1, 0)), SOUTH(new Position(0, 1)), WEST(new Position(-1, 0)), NORTH_EAST(new Position(1, -1)), SOUTH_EAST(new Position(1, 1)), SOUTH_WEST(
			new Position(-1, 1)), NORTH_WEST(new Position(-1, -1));

	public static final EnumSet<Orientation> Simple = EnumSet.of(NORTH, EAST, SOUTH, WEST);

	public static final FastMap<Orientation, Orientation> OPPOSITS = new FastMap<Orientation, Orientation>() {
		{
			this.put(NORTH, SOUTH);
			this.put(SOUTH, NORTH);
			this.put(EAST, WEST);
			this.put(WEST, EAST);
			this.put(NORTH_EAST, SOUTH_WEST);
			this.put(SOUTH_WEST, NORTH_EAST);
			this.put(NORTH_WEST, SOUTH_EAST);
			this.put(SOUTH_EAST, NORTH_WEST);
		}
	}.unmodifiable();

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
