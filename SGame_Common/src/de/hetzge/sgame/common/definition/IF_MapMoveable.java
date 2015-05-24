package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;

public interface IF_MapMoveable {

	PathPosition getPathPosition();

	IF_Position_Immutable getCenteredPosition();

	float getSpeed();

	void move(Orientation orientation, float distanceInPixel);

	void setPath(Path path);

	void unsetPath();

	default IF_Coordinate_Immutable getGoalCollisionCoordinate() {
		PathPosition pathPosition = this.getPathPosition();
		if (pathPosition != null) {
			Path path = pathPosition.getPath();
			if (path != null) {
				return path.getGoalCollisionCoordinate();
			}
		}
		return null;
	}

	default boolean hasPath() {
		return this.getPathPosition() != null && this.getPathPosition().getPath() != null;
	}

}
