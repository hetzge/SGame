package de.hetzge.sgame.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;

public class ActiveEntityMap extends ActiveMap<Entity> {

	public ActiveEntityMap() {
		super();
	}

	public Collection<Entity> getFixedEntities(IF_Coordinate_Immutable coordinate) {
		return this.getConnectedObjects(coordinate.getColumn(), coordinate.getRow()).stream().filter(entity -> entity.isFixedPosition()).collect(Collectors.toList());
	}

	public Collection<Entity> getFlexibleEntities(IF_Coordinate_Immutable coordinate) {
		return this.getConnectedObjects(coordinate.getColumn(), coordinate.getRow()).stream().filter(entity -> !entity.isFixedPosition()).collect(Collectors.toList());
	}

}
