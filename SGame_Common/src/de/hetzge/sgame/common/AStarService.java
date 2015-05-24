package de.hetzge.sgame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

public final class AStarService {

	private static final XY[] EMPTY_TRAMPOLINE = new XY[0];
	private static final XY[] DONE_TRAMPOLINE = new XY[0];

	public AStarService() {
	}

	/**
	 * call with current rating 1 and changed start goal coordinates
	 */
	private XY[] rate(int[][] rating, IF_Collision mapCollision, int currentRating, IF_Coordinate_Immutable startRatePosition, IF_Coordinate_Immutable goalRatePosition) {
		int x = startRatePosition.getColumn();
		int y = startRatePosition.getRow();

		int goalX = goalRatePosition.getColumn();
		int goalY = goalRatePosition.getRow();

		XY[] next = new XY[4];

		try {

			if (rating.length == 0) {
				throw new IllegalStateException("map has no size");
			}

			if (x < 0 || x >= rating.length || y < 0 || y >= rating[0].length) {
				return AStarService.EMPTY_TRAMPOLINE;
			}

			if (mapCollision.isCollision(x, y)) {
				return AStarService.EMPTY_TRAMPOLINE;
			}

			if (rating[x][y] > 0) {
				return AStarService.EMPTY_TRAMPOLINE;
			}

			if (x == goalX && y == goalY) {
				return AStarService.DONE_TRAMPOLINE;
			}

			rating[x][y] = currentRating;

			int mapWidthInCollisionTiles = rating.length;
			int mapHeightInCollisionTiles = rating[0].length;

			int nextX;
			int nextY;

			nextX = x - 1;
			nextY = y;
			if (nextX > 0) {
				next[0] = new XY(nextX, nextY);
			}

			nextX = x;
			nextY = y - 1;
			if (nextY > 0) {
				next[1] = new XY(nextX, nextY);
			}

			nextX = x + 1;
			nextY = y;
			if (nextX < mapWidthInCollisionTiles) {
				next[2] = new XY(nextX, nextY);
			}

			nextX = x;
			nextY = y + 1;
			if (nextY < mapHeightInCollisionTiles) {
				next[3] = new XY(nextX, nextY);
			}

			return next;

		} catch (Exception ex) {
			throw new IllegalStateException("Exception while pathfinding at (" + x + "|" + y + ") with goal (" + goalX + "|" + goalY + ") on size (" + rating.length + "|" + rating[0].length + ")", ex);
		}
	}

	public List<IF_Coordinate_Immutable> evaluatePath(int[][] rating, int x, int y, int goalX, int goalY) {

		List<IF_Coordinate_Immutable> result = new ArrayList<>(rating[x][y]);

		int mapWidthInCollisionTiles = rating.length;
		int mapHeightInCollisionTiles = rating[0].length;

		int maxValue = Integer.MAX_VALUE;
		int nextSelectionX = x;
		int nextSelectionY = y;
		int nextX;
		int nextY;

		while (x != goalX || y != goalY) {

			nextX = x - 1;
			nextY = y;
			if (this.checkNext(nextX, nextY, goalX, goalY, rating, maxValue, mapWidthInCollisionTiles, mapHeightInCollisionTiles)) {
				maxValue = rating[nextX][nextY];
				nextSelectionX = nextX;
				nextSelectionY = nextY;
			}

			nextX = x;
			nextY = y - 1;
			if (this.checkNext(nextX, nextY, goalX, goalY, rating, maxValue, mapWidthInCollisionTiles, mapHeightInCollisionTiles)) {
				maxValue = rating[nextX][nextY];
				nextSelectionX = nextX;
				nextSelectionY = nextY;
			}

			nextX = x + 1;
			nextY = y;
			if (this.checkNext(nextX, nextY, goalX, goalY, rating, maxValue, mapWidthInCollisionTiles, mapHeightInCollisionTiles)) {
				maxValue = rating[nextX][nextY];
				nextSelectionX = nextX;
				nextSelectionY = nextY;
			}

			nextX = x;
			nextY = y + 1;
			if (this.checkNext(nextX, nextY, goalX, goalY, rating, maxValue, mapWidthInCollisionTiles, mapHeightInCollisionTiles)) {
				maxValue = rating[nextX][nextY];
				nextSelectionX = nextX;
				nextSelectionY = nextY;
			}

			if (nextSelectionX == x && nextSelectionY == y) {
				throw new IllegalStateException("Path not possible from (" + x + "|" + y + ") with goal (" + goalX + "|" + goalY + ") on size (" + rating.length + "|" + rating[0].length + ")\n" + Util.toMapString(rating, x, y));
			}

			x = nextSelectionX;
			y = nextSelectionY;

			IF_Coordinate_Immutable coordinate = new XY(x, y);
			result.add(coordinate);
		}

		result.add(new XY(goalX, goalY));

		return result;
	}

	private boolean checkNext(int nextX, int nextY, int goalX, int goalY, int[][] rating, int maxValue, int mapWidthInCollisionTiles, int mapHeightInCollisionTiles) {
		return (nextX == goalX && nextY == goalY) || (nextX > 0 && nextX < mapWidthInCollisionTiles && nextY > 0 && nextY < mapHeightInCollisionTiles && rating[nextX][nextY] > 0 && rating[nextX][nextY] < maxValue);
	}

	/**
	 * Uses the fix entity collision map.
	 */
	public Path findPath(IF_Map map, int startX, int startY, int goalX, int goalY) {
		return this.findPath(map, map.getFixEntityCollisionMap(), startX, startY, goalX, goalY);
	}

	public Path findPath(IF_Map map, IF_Collision collision, int startX, int startY, int goalX, int goalY) {
		IF_Collision mapCollision = collision;

		int[][] rating = new int[mapCollision.getWidthInTiles()][];
		for (int x = 0; x < rating.length; x++) {
			rating[x] = new int[mapCollision.getHeightInTiles()];
		}

		int step = 1;

		IF_Coordinate_Immutable start = new XY(startX, startY);
		IF_Coordinate_Immutable goal = new XY(goalX, goalY);

		List<XY[]> trampolines;
		List<XY[]> nextTramponlines = new LinkedList<>();

		XY[] firstTrampoline = this.rate(rating, mapCollision, step, goal, start);
		nextTramponlines.add(firstTrampoline);

		boolean found = false;
		loop: while (!found) {
			trampolines = nextTramponlines;
			nextTramponlines = new LinkedList<>();

			if (trampolines.isEmpty()) {
				break loop;
			}
			for (XY[] trampoline : trampolines) {
				for (int i = 0; i < trampoline.length; i++) {
					XY nextXY = trampoline[i];
					if (nextXY == null) {
						continue;
					}
					XY[] rate = this.rate(rating, mapCollision, step, nextXY, start);
					if (rate == AStarService.EMPTY_TRAMPOLINE) {
					} else if (rate == AStarService.DONE_TRAMPOLINE) {
						found = true;
						break loop;
					} else {
						nextTramponlines.add(rate);
					}
				}
				step++;
			}
		}

		if (found) {
			List<IF_Coordinate_Immutable> path = this.evaluatePath(rating, startX, startY, goalX, goalY);
			return new Path(start, goal, path);
		} else {
			return null;
		}

	}

}
