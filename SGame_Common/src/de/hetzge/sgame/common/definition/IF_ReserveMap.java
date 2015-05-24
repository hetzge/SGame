package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;

public interface IF_ReserveMap {

	boolean isReserved(IF_Coordinate_Immutable coordinate);

	void reserve(IF_Coordinate_Immutable coordinate, IF_MapMoveable moveable);

	void unreserve(IF_Coordinate_Immutable coordinate, IF_MapMoveable moveable);

}
