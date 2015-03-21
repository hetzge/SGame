package de.hetzge.sgame.common;

import java.util.EnumSet;

import javolution.util.FastMap;
import de.hetzge.sgame.common.newgeometry.XY;

public enum Orientation {

	// important that north, east, south and west are first
	NORTH(new XY(0, -1)), EAST(new XY(1, 0)), SOUTH(new XY(0, 1)), WEST(new XY(-1, 0)), NORTH_EAST(new XY(1, -1)), SOUTH_EAST(new XY(1, 1)), SOUTH_WEST(new XY(-1, 1)), NORTH_WEST(new XY(-1, -1));

	public static final EnumSet<Orientation> Simple = EnumSet.of(NORTH, EAST, SOUTH, WEST);
	public static final EnumSet<Orientation> Diagonal = EnumSet.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
	public static final EnumSet<Orientation> Vertical = EnumSet.of(NORTH, SOUTH);
	public static final EnumSet<Orientation> Horizontal = EnumSet.of(EAST, WEST);

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

	public final XY orientationFactor;

	/**
	 * Holds a optimized orientation factor for diagonal movement.
	 */
	public final XY orientationFactorOptimized;

	private Orientation(XY orientationFactor) {
		this.orientationFactor = orientationFactor;
		if (orientationFactor.getX() != 0 && orientationFactor.getY() != 0) {
			this.orientationFactorOptimized = orientationFactor.copy().divide(new XY((float) Math.sqrt(2d) * orientationFactor.getX(), (float) Math.sqrt(2d) * orientationFactor.getY()));
		} else {
			this.orientationFactorOptimized = orientationFactor;
		}
	}

	public boolean isSimple(){
		return Orientation.Simple.contains(this);
	}

	public boolean isDiagonal(){
		return Orientation.Diagonal.contains(this);
	}

	public boolean isVertical(){
		return Orientation.Vertical.contains(this);
	}

	public boolean isHorizontal(){
		return Orientation.Horizontal.contains(this);
	}

}
