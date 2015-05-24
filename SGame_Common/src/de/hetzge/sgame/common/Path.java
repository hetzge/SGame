package de.hetzge.sgame.common;

import java.io.Serializable;
import java.util.List;

import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

public class Path implements Serializable {

	private final IF_Coordinate_Immutable start;
	private final IF_Coordinate_Immutable goal;

	private float[] x;
	private float[] y;

	public Path(IF_Coordinate_Immutable start, IF_Coordinate_Immutable goal, List<? extends IF_Coordinate_Immutable> path) {
		this.start = start;
		this.goal = goal;
		if (path != null) {
			this.x = new float[path.size()];
			this.y = new float[path.size()];
		}
		for (int i = 0; i < path.size(); i++) {
			IF_Coordinate_Immutable position = path.get(i);
			this.x[i] = position.getColumn();
			this.y[i] = position.getRow();
		}
	}

	public IF_Coordinate_Immutable getStartCollisionCoordinate() {
		return this.start;
	}

	public IF_Coordinate_Immutable getGoalCollisionCoordinate() {
		return this.goal;
	}

	public IF_Coordinate_Immutable getPathCollisionCoordinate(int i) {
		return new XY(this.x[i], this.y[i]);
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

}
