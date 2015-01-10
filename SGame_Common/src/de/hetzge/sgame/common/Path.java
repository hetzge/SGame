package de.hetzge.sgame.common;

import java.io.Serializable;
import java.util.List;

import de.hetzge.sgame.common.geometry.Position;

public class Path implements Serializable {

	private final Position start;
	private final Position goal;

	private final List<Position> path;

	public Path(Position start, Position goal, List<Position> path) {
		this.start = start;
		this.goal = goal;
		this.path = path;
	}

	public Position getStart() {
		return this.start;
	}

	public Position getGoal() {
		return this.goal;
	}

	public List<Position> getPath() {
		return this.path;
	}

	public int getPathLength() {
		return this.path.size();
	}

	public boolean isPathNotPossible() {
		return this.start == null || this.goal == null || this.path == null || this.getPathLength() <= 0;
	}

}
