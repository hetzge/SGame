package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;

public interface IF_ReserveMap {

	boolean isReserved(IF_Coordinate_ImmutableView coordinate);

	void reserve(IF_Coordinate_ImmutableView coordinate, IF_MapMoveable moveable);

	void unreserve(IF_Coordinate_ImmutableView coordinate, IF_MapMoveable moveable);

}
