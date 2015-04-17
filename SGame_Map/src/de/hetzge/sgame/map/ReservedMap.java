package de.hetzge.sgame.map;

import java.util.HashMap;
import java.util.Map;

import de.hetzge.sgame.common.definition.IF_MapMoveable;
import de.hetzge.sgame.common.definition.IF_ReserveMap;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;

public class ReservedMap implements IF_ReserveMap {

	private final Map<IF_Coordinate_ImmutableView, IF_MapMoveable> reservedMap = new HashMap<>();

	@Override
	public synchronized boolean isReserved(IF_Coordinate_ImmutableView coordinate) {
		IF_MapMoveable moveable = this.reservedMap.get(coordinate);
		return moveable != null;
	}

	@Override
	public void reserve(IF_Coordinate_ImmutableView coordinate, IF_MapMoveable moveable) {
		this.reservedMap.put(coordinate, moveable);
	}

	@Override
	public synchronized void unreserve(IF_Coordinate_ImmutableView coordinate, IF_MapMoveable moveable) {
		IF_MapMoveable reservedMoveable = this.reservedMap.get(coordinate);
		if (reservedMoveable != null && moveable == reservedMoveable) {
			this.reservedMap.remove(coordinate);
		}
	}

}
