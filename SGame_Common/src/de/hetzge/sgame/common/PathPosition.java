package de.hetzge.sgame.common;

import java.io.Serializable;

import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.Position;

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

	public Position getCurrentWaypoint() {
		return this.path.getPathPosition(this.positionOnPath);
	}

	public float getDistanceToWaypointBefore() {
		if (this.isOnStartOfPath()) {
			return 0f;
		}
		Position waypointBefore = this.path.getPathPosition(this.positionOnPath - 1);
		return waypointBefore.distance(this.getCurrentWaypoint());
	}

	public boolean continueOnPath(IF_ImmutablePosition<?> position) {
		if (!this.isOnEndOfPath() && this.getCurrentWaypoint().distance(position) < 1F) {
			this.moveForward();
			return true;
		}
		return false;
	}

	public boolean reachedEndOfPath(IF_ImmutablePosition<?> position) {
		return this.isOnEndOfPath() && this.getCurrentWaypoint().distance(position) < 1F;
	}

}
