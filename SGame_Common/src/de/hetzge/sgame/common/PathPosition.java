package de.hetzge.sgame.common;

import java.io.Serializable;

import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.IF_Position;

public class PathPosition implements Serializable {

	private final Path path;
	private int positionOnPath;

	public PathPosition(Path path, int positionOnPath) {
		this.path = path;
		this.positionOnPath = positionOnPath;
	}

	public int getPositionOnPath() {
		return this.positionOnPath;
	}

	public Path getPath() {
		return this.path;
	}

	public boolean isOnEndOfPath() {
		return this.positionOnPath >= this.path.getPathLength() - 1;
	}

	public boolean isOnStartOfPath() {
		return this.positionOnPath <= 0;
	}

	public void moveForward() {
		if (this.positionOnPath + 1 >= this.path.getPathLength()) {
			this.positionOnPath = this.path.getPathLength() - 1;
			return;
		} else {
			this.positionOnPath++;
		}
	}

	public void moveBackward() {
		if (this.positionOnPath - 1 < 0) {
			this.positionOnPath = 0;
			return;
		} else {
			this.positionOnPath--;
		}
	}

	public IF_Coordinate getCurrentCollisionCoordinate() {
		return this.path.getPathCollisionCoordinate(this.positionOnPath);
	}

	public IF_Position getCurrentPosition() {
		return this.path.getPathPosition(this.positionOnPath);
	}

	public Orientation getOrientationFromWaypointBeforeToNext() {
		if (this.isOnEndOfPath()) {
			return Orientation.DEFAULT;
		}
		IF_Position waypointBefore = this.path.getPathPosition(this.positionOnPath - 1);
		return waypointBefore.orientationToOther(this.getCurrentCollisionCoordinate());
	}

	public float getDistanceToWaypointBefore() {
		if (this.isOnStartOfPath()) {
			return 0f;
		}
		IF_Position waypointBefore = this.path.getPathPosition(this.positionOnPath - 1);
		return waypointBefore.distance(this.getCurrentCollisionCoordinate());
	}

	public boolean continueOnPath(IF_Position position) {
		if (!this.isOnEndOfPath() && this.getCurrentCollisionCoordinate().distance(position) < 1F) {
			this.moveForward();
			return true;
		}
		return false;
	}

	public boolean reachedEndOfPath(IF_Position position) {
		return this.isOnEndOfPath() && this.getCurrentCollisionCoordinate().distance(position) < 1F;
	}

}
