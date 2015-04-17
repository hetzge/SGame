package de.hetzge.sgame.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;

public class Path implements Serializable, Iterable<IF_Coordinate_ImmutableView> {

	private final IF_Map map;

	private final IF_Coordinate_ImmutableView start;
	private final IF_Coordinate_ImmutableView goal;

	private float[] x;
	private float[] y;

	public Path(IF_Coordinate_ImmutableView start, IF_Coordinate_ImmutableView goal, List<? extends IF_Coordinate_ImmutableView> path, IF_Map map) {
		this.map = map;
		this.start = start;
		this.goal = goal;
		if (path != null) {
			this.x = new float[path.size()];
			this.y = new float[path.size()];
		}
		for (int i = 0; i < path.size(); i++) {
			IF_Coordinate_ImmutableView position = path.get(i);
			this.x[i] = position.getX();
			this.y[i] = position.getY();
		}
	}

	public IF_Coordinate_ImmutableView getStartCollisionCoordinate() {
		return this.start;
	}

	public IF_Coordinate_ImmutableView getGoalCollisionCoordinate() {
		return this.goal;
	}

	public IF_Coordinate getPathCollisionCoordinate(int i) {
		return new XY(this.x[i], this.y[i]);
	}

	public IF_Position_ImmutableView getStartPosition(){
		return this.map.convertCollisionTileXYInPxXY(this.getStartCollisionCoordinate());
	}

	public IF_Position_ImmutableView getEndPosition(){
		return this.map.convertCollisionTileXYInPxXY(this.getGoalCollisionCoordinate());
	}

	public IF_Position_ImmutableView getPathPosition(int i){
		return this.map.convertCollisionTileXYInPxXY(this.getPathCollisionCoordinate(i)); // TODO Mitte im Tile
	}

	public int getPathLength() {
		return this.x.length;
	}

	public boolean isPathPossible(){
		return !this.isPathNotPossible();
	}

	public boolean isPathNotPossible() {
		return this.start == null || this.goal == null || this.x == null || this.y == null || this.getPathLength() <= 0;
	}

	@Override
	public Iterator<IF_Coordinate_ImmutableView> iterator() {
		final PathPosition pathPosition = new PathPosition(this);
		return new Iterator<IF_Coordinate_ImmutableView>() {

			@Override
			public IF_Coordinate_ImmutableView next() {
				IF_Coordinate_ImmutableView currentCollisionCoordinate = pathPosition.getCurrentCollisionCoordinate();
				pathPosition.moveForward();
				return currentCollisionCoordinate;
			}

			@Override
			public boolean hasNext() {
				return !pathPosition.isOnEndOfPath();
			}
		};
	}

}
