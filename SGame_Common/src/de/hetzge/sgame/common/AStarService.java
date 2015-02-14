package de.hetzge.sgame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.newgeometry.IF_Coordinate;
import de.hetzge.sgame.common.newgeometry.XY;

public final class AStarService {

	private static final XY[] EMPTY_TRAMPOLINE = new XY[0];
	private static final XY[] DONE_TRAMPOLINE = new XY[0];

	private final IF_MapProvider mapProvider;

	public AStarService(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	/**
	 * call with current rating 1 and changed start goal coordinates
	 */
	private XY[] rate(int[][] rating, IF_Collision mapCollision, int currentRating, IF_Coordinate startRatePosition, IF_Coordinate goalRatePosition, boolean[][] entityCollision) {
		// TODO entityCollision

		int x = startRatePosition.getIX();
		int y = startRatePosition.getIY();

		int goalX = goalRatePosition.getIX();
		int goalY = goalRatePosition.getIY();

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

	public List<IF_Coordinate> evaluatePath(int[][] rating, int x, int y, int goalX, int goalY) {

		List<IF_Coordinate> result = new ArrayList<>(rating[x][y]);

		int mapWidthInCollisionTiles = rating.length;
		int mapHeightInCollisionTiles = rating[0].length;

		int maxValue = Integer.MAX_VALUE;
		int nextSelectionX = x;
		int nextSelectionY = y;
		int nextX;
		int nextY;

		while (x != goalX && y != goalY) {

			nextX = x - 1;
			nextY = y;
			if (nextX > 0 && rating[nextX][nextY] > 0) {
				if (rating[nextX][nextY] < maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x;
			nextY = y - 1;
			if (nextY > 0 && rating[nextX][nextY] > 0) {
				if (rating[nextX][nextY] < maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x + 1;
			nextY = y;
			if (nextX < mapWidthInCollisionTiles && rating[nextX][nextY] > 0) {
				if (rating[nextX][nextY] < maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x;
			nextY = y + 1;
			if (nextY < mapHeightInCollisionTiles && rating[nextX][nextY] > 0) {
				if (rating[nextX][nextY] < maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			if (nextSelectionX == x && nextSelectionY == y) {
				throw new IllegalStateException("Path not possible from (" + x + "|" + y + ") with goal (" + goalX + "|" + goalY + ") on size (" + rating.length + "|" + rating[0].length + ")\n" + Util.toMapString(rating, x, y));
			}

			x = nextSelectionX;
			y = nextSelectionY;

			IF_Coordinate coordinate = new XY(x, y);
			result.add(coordinate);
		}

		return result;
	}

	public Path findPath(IF_Collision mapCollision, int startX, int startY, int goalX, int goalY, boolean[][] collision) {

		int[][] rating = new int[mapCollision.getWidthInTiles()][];
		for (int x = 0; x < rating.length; x++) {
			rating[x] = new int[mapCollision.getHeightInTiles()];
		}

		int step = 1;

		IF_Coordinate start = new XY(startX, startY);
		IF_Coordinate goal = new XY(goalX, goalY);

		List<XY[]> trampolines;
		List<XY[]> nextTramponlines = new LinkedList<>();

		XY[] firstTrampoline = this.rate(rating, mapCollision, step, goal, start, collision);
		nextTramponlines.add(firstTrampoline);

		List<IF_Coordinate> path = new LinkedList<>();

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
					XY[] rate = this.rate(rating, mapCollision, step, nextXY, start, collision);
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
			path = this.evaluatePath(rating, startX, startY, goalX, goalY);
			return new Path(start, goal, path, this.mapProvider.provide());
		} else {
			return null;
		}

	}

}
