package de.hetzge.sgame.common.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.FPS;
import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.definition.IF_MapMoveable;
import de.hetzge.sgame.common.definition.IF_ReserveMap;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;

/**
 * TODO move to SGame_Map ?!
 * 
 * @author Markus
 *
 */
public class MoveOnMapService {

	private final IF_ReserveMap reserveMap;
	private final IF_MapProvider mapProvider;

	public MoveOnMapService(IF_ReserveMap reserveMap, IF_MapProvider mapProvider) {
		this.reserveMap = reserveMap;
		this.mapProvider = mapProvider;
	}

	public void move(IF_MapMoveable moveable) {
		if (!moveable.hasPath()) {
			return;
		}

		PathPosition pathPosition = moveable.getPathPosition();
		Path path = pathPosition.getPath();

		IF_Position_Immutable centeredPosition = moveable.getCenteredPosition();
		IF_Position_Immutable currentGoal = this.mapProvider.provide().convertCollisionTileXYInPxXY(path.getPathCollisionCoordinate(pathPosition.getPositionOnPath()));

		float distanceInPixel = moveable.getSpeed() * FPS.ticks();

		if (!this.isPositionReached(centeredPosition, currentGoal, distanceInPixel)) {
			Orientation orientationToOther = currentGoal.orientationTo(centeredPosition);
			moveable.move(orientationToOther, distanceInPixel);
		} else {
			pathPosition.moveForward();
		}
	}

	public boolean reachedGoal(IF_MapMoveable moveable) {
		if (!moveable.hasPath()) {
			return false;
		}

		PathPosition pathPosition = moveable.getPathPosition();
		boolean isOnEndOfPath = pathPosition.isOnEndOfPath();
		if (isOnEndOfPath) {
			moveable.unsetPath();
			return true;
		}
		return false;
	}

	public void setPath(IF_MapMoveable moveable, Path path) {
		moveable.setPath(path);
	}

	public void unsetPath(IF_MapMoveable moveable) {
		IF_Coordinate_Immutable goalCollisionCoordinate = moveable.getGoalCollisionCoordinate();
		if (goalCollisionCoordinate != null) {
			this.reserveMap.unreserve(goalCollisionCoordinate, moveable);
			moveable.unsetPath();
		}
	}

	// TODO use and move to somewhere else
	private List<Orientation> evaluateDisplacementOrientations(Orientation orientation) {
		List<Orientation> displacementOrientations = new LinkedList<>();
		if (orientation.isHorizontal() || orientation.isDiagonal()) {
			displacementOrientations.addAll(Orientation.Vertical);
		} else {
			displacementOrientations.addAll(Orientation.Horizontal);
		}
		Collections.shuffle(displacementOrientations);
		displacementOrientations.add(Orientation.OPPOSITS.get(orientation));

		return displacementOrientations;
	}

	private boolean isPositionReached(IF_Position_Immutable position, IF_Position_Immutable goalPosition, float speed) {
		float xDif = Math.abs(position.getFX() - goalPosition.getFX());
		float yDif = Math.abs(position.getFY() - goalPosition.getFY());
		return xDif <= speed && yDif <= speed;
	}

}
