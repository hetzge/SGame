package de.hetzge.sgame.common;

import java.io.Serializable;

import de.hetzge.sgame.common.newgeometry.views.IF_Coordinate_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;

public class PathPosition implements Serializable {

	private final Path path;
	private int positionOnPath;

	private IF_Coordinate_ImmutableView currentCollisionCoordinate;
	private IF_Position_ImmutableView currentPosition;
	private Orientation orientationToCurrentPosition;
	private float distanceToCurrentPosition;

	public PathPosition(Path path) {
		this(path, 0);
	}

	public PathPosition(Path path, int positionOnPath) {
		this.path = path;
		this.positionOnPath = positionOnPath;
		this.onChangePathPosition();
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
		} else {
			this.positionOnPath++;
			this.onChangePathPosition();
		}
	}

	public void moveBackward() {
		if (this.positionOnPath - 1 < 0) {
			this.positionOnPath = 0;
		} else {
			this.positionOnPath--;
			this.onChangePathPosition();
		}
	}

	public IF_Coordinate_ImmutableView getCurrentCollisionCoordinate() {
		return this.currentCollisionCoordinate;
	}

	public IF_Position_ImmutableView getCurrentPosition() {
		return this.currentPosition;
	}

	public Orientation getOrientationFromWaypointBeforeToNext() {
		return this.orientationToCurrentPosition;
	}

	public float getDistanceToWaypointBefore() {
		return this.distanceToCurrentPosition;
	}

	private void onChangePathPosition() {
		// collision coordinate
		this.currentCollisionCoordinate = this.path.getPathCollisionCoordinate(this.positionOnPath);

		// position
		this.currentPosition = this.path.getPathPosition(this.positionOnPath);

		// orientation
		if (this.isOnEndOfPath()) {
			this.orientationToCurrentPosition = Orientation.DEFAULT;
		} else if (!this.isOnStartOfPath()) {
			IF_Position_ImmutableView waypointBefore = this.path.getPathPosition(this.positionOnPath - 1);
			this.orientationToCurrentPosition = this.getCurrentPosition().orientationToOther(waypointBefore);
		}

		// distance
		if (this.isOnStartOfPath()) {
			this.distanceToCurrentPosition = 0f;
		} else {
			IF_Position_ImmutableView waypointBefore = this.path.getPathPosition(this.positionOnPath - 1);
			this.distanceToCurrentPosition = waypointBefore.distance(this.getCurrentPosition());
		}
	}

}
