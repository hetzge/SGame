package de.hetzge.sgame.common;

import java.io.Serializable;
import java.util.List;

import de.hetzge.sgame.common.geometry.Position;

public class Path implements Serializable {

	private final Position start;
	private Position goal;

	private float[] x;
	private float[] y;

	public Path(Position start, Position goal, List<Position> path) {
		this.start = start;
		this.goal = goal;
		if (path != null) {
			this.x = new float[path.size()];
			this.y = new float[path.size()];
		}
		for (int i = 0; i < path.size(); i++) {
			Position position = path.get(i);
			this.x[i] = position.getX();
			this.y[i] = position.getY();
		}
	}

	public void add(Path path) {
		float[] oldX = this.x;
		float[] oldY = this.y;
		this.x = new float[oldX.length + path.getPathLength()];
		this.y = new float[oldY.length + path.getPathLength()];
		for (int i = 0; i < oldX.length; i++) {
			this.x[i] = oldX[i];
			this.y[i] = oldY[i];
		}
		for (int i = oldX.length; i < oldX.length + path.getPathLength(); i++) {
			this.x[i] = path.x[i - oldX.length];
			this.y[i] = path.y[i - oldX.length];
		}
		this.goal = path.goal;
	}

	public Position getStart() {
		return this.start;
	}

	public Position getGoal() {
		return this.goal;
	}

	public Position getPathPosition(int i) {
		return new Position(this.x[i], this.y[i]);
	}

	// public List<Position> getPath() {
	// List<Position> pathPositions = new ArrayList<>(this.getPathLength());
	// for (int i = 0; i < this.getPathLength(); i++) {
	// pathPositions.add(new Position(this.x[i], this.y[i]));
	// }
	// return pathPositions;
	// }

	public int getPathLength() {
		return this.x.length;
	}

	public boolean isPathNotPossible() {
		return this.start == null || this.goal == null || this.x == null || this.y == null || this.getPathLength() <= 0;
	}

}
