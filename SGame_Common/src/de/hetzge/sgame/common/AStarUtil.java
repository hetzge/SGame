package de.hetzge.sgame.common;

import java.util.LinkedList;

import de.hetzge.sgame.common.geometry.Position;

public final class AStarUtil {

	private AStarUtil() {
	}

	/**
	 * call with current rating 1 and changed start goal coordinates
	 */
	public static boolean rate(int[][] rating, int currentRating, int x, int y, int goalX, int goalY, boolean[][] entityCollision) {
		// TODO entityCollision

		if (rating.length == 0)
			throw new IllegalStateException("map has no size");

		if (rating[x][y] > 0) {
			return false;
		}

		if (x == goalX && y == goalY) {
			return true;
		}

		rating[x][y] = currentRating;
		System.out.println("rate " + x + "/" + y + " -> " + currentRating);

		int mapWidthInCollisionTiles = rating.length;
		int mapHeightInCollisionTiles = rating[0].length;

		int nextX;
		int nextY;

		nextX = x - 1;
		nextY = y;
		if (nextX > 0) {
			if (AStarUtil.rate(rating, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		nextX = x;
		nextY = y - 1;
		if (nextY > 0) {
			if (AStarUtil.rate(rating, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		nextX = x + 1;
		nextY = y;
		if (nextX < mapWidthInCollisionTiles) {
			if (AStarUtil.rate(rating, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		nextX = x;
		nextY = y + 1;
		if (nextY < mapHeightInCollisionTiles) {
			if (AStarUtil.rate(rating, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		return false;
	}

	public static LinkedList<Position> evaluatePath(int[][] rating, int x, int y, int goalX, int goalY) {

		LinkedList<Position> result = new LinkedList<>();

		int mapWidthInCollisionTiles = rating.length;
		int mapHeightInCollisionTiles = rating[0].length;

		int maxValue = 0;
		int nextSelectionX = x;
		int nextSelectionY = y;
		int nextX;
		int nextY;

		while (x != goalX && y != goalY) {

			nextX = x - 1;
			nextY = y;
			if (nextX > 0) {
				if (rating[nextX][nextY] <= maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x;
			nextY = y - 1;
			if (nextY > 0) {
				if (rating[nextX][nextY] <= maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x + 1;
			nextY = y;
			if (nextX < mapWidthInCollisionTiles) {
				if (rating[nextX][nextY] <= maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			nextX = x;
			nextY = y + 1;
			if (nextY < mapHeightInCollisionTiles) {
				if (rating[nextX][nextY] <= maxValue) {
					maxValue = rating[nextX][nextY];
					nextSelectionX = nextX;
					nextSelectionY = nextY;
				}
			}

			x = nextSelectionX;
			y = nextSelectionY;

			float xInPx = CommonConfig.INSTANCE.map.convertCollisionTileInPx(x);
			float yInPx = CommonConfig.INSTANCE.map.convertCollisionTileInPx(y);
			Position position = new Position(xInPx, yInPx);
			result.add(position);
		}

		return result;
	}

	public static void main(String[] args) {
		int[][] rating = new int[100][];
		for (int x = 0; x < 100; x++) {
			rating[x] = new int[100];
		}
		if (AStarUtil.rate(rating, 1, 90, 90, 10, 10, new boolean[0][])) {
			LinkedList<Position> path = AStarUtil.evaluatePath(rating, 10, 10, 90, 90);
			for (Position position : path) {
				System.out.println(position);
			}
		}
	}

}
