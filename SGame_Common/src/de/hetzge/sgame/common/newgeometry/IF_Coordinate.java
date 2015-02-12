package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_MutableView;

public interface IF_Coordinate extends IF_Coordinate_ImmutableView, IF_Coordinate_MutableView {
	@Override
	public default void setIX(int x) {
		this.setX(x);
	}

	@Override
	public default void setIY(int y) {
		this.setY(y);
	}

	@Override
	public default int getIX() {
		return (int) Math.floor(this.getX());
	}

	@Override
	public default int getIY() {
		return (int) Math.floor(this.getY());
	}
}
