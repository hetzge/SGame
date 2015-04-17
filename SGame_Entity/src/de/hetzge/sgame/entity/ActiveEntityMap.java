package de.hetzge.sgame.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;

public class ActiveEntityMap extends ActiveMap<Entity> {

	public ActiveEntityMap() {
		super();
	}

	public Collection<Entity> getFixedEntities(IF_Coordinate_ImmutableView coordinate) {
		return this.getConnectedObjects(coordinate.getIX(), coordinate.getIY()).stream().filter(entity -> entity.isFixedPosition()).collect(Collectors.toList());
	}

	public Collection<Entity> getFlexibleEntities(IF_Coordinate_ImmutableView coordinate) {
		return this.getConnectedObjects(coordinate.getIX(), coordinate.getIY()).stream().filter(entity -> !entity.isFixedPosition()).collect(Collectors.toList());
	}

}
