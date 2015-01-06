package de.hetzge.sgame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.Position;

public final class AStarUtil {

	private AStarUtil() {
	}

	/**
	 * call with current rating 1 and changed start goal coordinates
	 */
	public static boolean rate(int[][] rating, IF_Collision mapCollision, int currentRating, int x, int y, int goalX, int goalY, boolean[][] entityCollision) {
		// TODO entityCollision

		if (rating.length == 0) {
			throw new IllegalStateException("map has no size");
		}

		if (mapCollision.isCollision(x, y)) {
			return false;
		}

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
			if (AStarUtil.rate(rating, mapCollision, ++currentRating, nextX, nextY, goalX, goalY, entityCollision)) {
				return true;
			}
		}

		nextX = x;
		nextY = y - 1;
		if (nextY > 0) {
			if (AStarUtil.rate(rating, mapCollision, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		nextX = x + 1;
		nextY = y;
		if (nextX < mapWidthInCollisionTiles) {
			if (AStarUtil.rate(rating, mapCollision, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		nextX = x;
		nextY = y + 1;
		if (nextY < mapHeightInCollisionTiles) {
			if (AStarUtil.rate(rating, mapCollision, ++currentRating, nextX, nextY, goalX, goalY, entityCollision))
				return true;
		}

		return false;
	}

	public static List<Position> evaluatePath(int[][] rating, int x, int y, int goalX, int goalY) {

		List<Position> result = new ArrayList<>(rating[x][y]);

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

	public static Path findPath(IF_Collision mapCollision, int startX, int startY, int goalX, int goalY, boolean[][] collision) {
		int[][] rating = new int[mapCollision.getWidthInTiles()][];
		for (int x = 0; x < 100; x++) {
			rating[x] = new int[mapCollision.getHeightInTiles()];
		}

		List<Position> path = new LinkedList<>();
		if (AStarUtil.rate(rating, mapCollision, 1, goalX, goalY, startX, startY, collision)) {
			path = AStarUtil.evaluatePath(rating, startX, startY, goalX, goalY);

			IF_Map map = CommonConfig.INSTANCE.map;

			Position startPositionInPx = new Position(map.convertCollisionTileInPx(startX), map.convertCollisionTileInPx(startY));
			Position goalPositionInPx = new Position(map.convertCollisionTileInPx(goalX), map.convertCollisionTileInPx(goalY));

			return new Path(startPositionInPx, goalPositionInPx, path);
		} else {
			return new Path(null, null, null);
		}

	}

	public static void main(String[] args) {

		Path path = AStarUtil.findPath(new IF_Collision() {

			@Override
			public void setCollision(int x, int y, boolean collision) {
			}

			@Override
			public boolean isCollision(int x, int y) {
				return false;
			}

			@Override
			public int getWidthInTiles() {
				return 100;
			}

			@Override
			public int getHeightInTiles() {
				return 100;
			}
		}, 10, 10, 100, 100, new boolean[0][]);
		for (Position position : path.getPath()) {
			System.out.println(position);
		}
	}
}
