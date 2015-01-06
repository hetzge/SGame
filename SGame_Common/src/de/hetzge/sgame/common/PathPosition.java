package de.hetzge.sgame.common;

import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.Position;

public class PathPosition {

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
		return this.positionOnPath >= this.path.getPathLength();
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
		return this.path.getPath().get(this.positionOnPath);
	}

	public float getDistanceToWaypointBefore() {
		if (this.isOnStartOfPath())
			return 0f;
		Position waypointBefore = this.path.getPath().get(this.positionOnPath - 1);
		return waypointBefore.distance(this.getCurrentWaypoint());
	}

	public boolean continueOnPath(IF_ImmutablePosition<?> position) {
		if (this.getCurrentWaypoint().distance(position) < 1f && !this.isOnEndOfPath()) {
			this.moveForward();
			return true;
		}
		return false;
	}
}
