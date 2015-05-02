package de.hetzge.sgame.common.hierarchical;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry.IF_Position;
import de.hetzge.sgame.common.newgeometry.XY;

public class HierarchicalMap2 implements Serializable {

	private class Area implements Serializable {

		private class AStarWaypoint extends XY implements IF_AStarWaypoint<AStarWaypoint> {
			public AStarWaypoint(int x, int y) {
				super(x, y);
			}

			@Override
			public List<AStarWaypoint> getWaypoints(AStarWaypoint waypoint) {
				LinkedList<AStarWaypoint> result = new LinkedList<>();

				int nextX;
				int nextY;

				nextX = waypoint.getIX() - 1;
				nextY = waypoint.getIY();
				if (Area.this.isAreaPosition(nextX, nextY) && !Area.this.isCollision(nextX, nextY)) {
					result.add(new AStarWaypoint(nextX, nextY));
				}

				nextX = waypoint.getIX();
				nextY = waypoint.getIY() - 1;
				if (Area.this.isAreaPosition(nextX, nextY) && !Area.this.isCollision(nextX, nextY)) {
					result.add(new AStarWaypoint(nextX, nextY));
				}

				nextX = waypoint.getIX() + 1;
				nextY = waypoint.getIY();
				if (Area.this.isAreaPosition(nextX, nextY) && !Area.this.isCollision(nextX, nextY)) {
					result.add(new AStarWaypoint(nextX, nextY));
				}

				nextX = waypoint.getIX();
				nextY = waypoint.getIY() + 1;
				if (Area.this.isAreaPosition(nextX, nextY) && !Area.this.isCollision(nextX, nextY)) {
					result.add(new AStarWaypoint(nextX, nextY));
				}

				return result;
			}

			public IF_Position asGlobalPosition() {
				return HierarchicalMap2.this.map.convertCollisionTileXYInPxXY(new XY(Area.this.startX, Area.this.startY).add(new XY(this.getIX(), this.getIY())));
			}

		}

		private class TransitionPointAnalyzer {
			private int startX, endX, startY, endY;

			public TransitionPointAnalyzer() {
				this.reset();
			}

			private void reset() {
				this.startX = this.endX = this.startY = this.endY = -1;
			}

			private void add(int x, int y) {
				if (this.startX == -1) {
					this.startX = x;
					this.startY = y;
				} else {
					this.endX = x;
					this.endY = y;
				}
			}

			private void end() {
				if (this.startX != -1) {
					if (this.endX == -1) {
						this.endX = this.startX;
						this.endY = this.startY;
					}

					TransitionPoint transitionPoint = new TransitionPoint(Area.this, this.startX, this.endX, this.startY, this.endY);
					Area.this.transitionPoints.add(transitionPoint);
					this.reset();
				}
			}
		}

		private class TransitionPoint implements Serializable, IF_AStarWaypoint<TransitionPoint> {
			private final int startX;
			private final int endX;
			private final int startY;
			private final int endY;

			private final Area area;
			private TransitionPoint partner;

			public TransitionPoint(Area area, int startX, int endX, int startY, int endY) {
				this.startX = startX;
				this.endX = endX;
				this.startY = startY;
				this.endY = endY;
				this.area = area;
			}

			private AStarWaypoint asAStarWaypoint() {
				return new AStarWaypoint((int) (this.startX + Math.floor((this.endX - this.startX) / 2f)), (this.startY + (int) Math.floor((this.endY - this.startY) / 2f)));
			}

			private void connectTo(TransitionPoint to) {
				this.partner = to;
				to.partner = this;
			}

			private boolean isPositionIn(int x, int y) {
				return this.startX <= x && this.startY <= y && this.endX >= x && this.endY >= y;
			}

			private boolean match(int startX, int startY, int endX, int endY) {
				return this.startX == startX && this.startY == startY && this.endX == endX && this.endY == endY;
			}

			@Override
			public List<TransitionPoint> getWaypoints(TransitionPoint transitionPoint) {
				List<TransitionPoint> transitionPoints = new LinkedList<>();
				Map<TransitionPoint, Path> connectedTransactionPoints = Area.this.transitionPointConnections.get(this);
				if (connectedTransactionPoints != null) {
					if (transitionPoint.partner != null) {
						transitionPoints.add(transitionPoint.partner);
					}
					Set<TransitionPoint> set = connectedTransactionPoints.keySet();
					transitionPoints.addAll(set);
				}
				return transitionPoints;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + this.getOuterType().hashCode();
				result = prime * result + this.endX;
				result = prime * result + this.endY;
				result = prime * result + this.startX;
				result = prime * result + this.startY;
				if (this.partner != null) {
					result = prime * result + this.partner.startX;
					result = prime * result + this.partner.startY;
					result = prime * result + this.partner.endX;
					result = prime * result + this.partner.endY;
				}
				result = prime * result + this.area.getAreaX();
				result = prime * result + this.area.getAreaY();
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (this.getClass() != obj.getClass()) {
					return false;
				}
				TransitionPoint other = (TransitionPoint) obj;
				if (!this.getOuterType().equals(other.getOuterType())) {
					return false;
				}
				if (this.endX != other.endX) {
					return false;
				}
				if (this.endY != other.endY) {
					return false;
				}
				if (this.startX != other.startX) {
					return false;
				}
				if (this.startY != other.startY) {
					return false;
				}
				if (this.partner == null && other.partner != this.partner) {
					return false;
				}

				if (this.partner != null) {
					if (this.partner.startX != other.partner.startX) {
						return false;
					}
					if (this.partner.startY != other.partner.startY) {
						return false;
					}
					if (this.partner.endX != other.partner.endX) {
						return false;
					}
					if (this.partner.endY != other.partner.endY) {
						return false;
					}
				}

				if (this.area.getAreaX() != other.area.getAreaX()) {
					return false;
				}
				if (this.area.getAreaY() != other.area.getAreaY()) {
					return false;
				}

				return true;
			}

			private Area getOuterType() {
				return Area.this;
			}

		}

		private class TransitionPointConnection implements Serializable {
			private final TransitionPoint from;
			private final TransitionPoint to;

			private final Path path;

			public TransitionPointConnection(TransitionPoint from, TransitionPoint to, Path path) {
				this.from = from;
				this.to = to;
				this.path = path;
			}
		}

		private final int startX;
		private final int startY;
		private final List<TransitionPoint> transitionPoints = new LinkedList<TransitionPoint>();
		private final Map<TransitionPoint, Map<TransitionPoint, Path>> transitionPointConnections = new HashMap<>();
		private boolean upToDate = false;

		public Area(int startX, int startY) {
			this.startX = startX;
			this.startY = startY;
		}

		private TransitionPoint findTransitionPoint(int startX, int startY, int endX, int endY) {
			for (TransitionPoint transitionPoint : this.transitionPoints) {
				if (transitionPoint.match(startX, startY, endX, endY)) {
					return transitionPoint;
				}
			}
			return null;
		}

		private void init() {

			LinkedList<Area> areas = new LinkedList<>();

			Area area;
			if ((area = this.getLeft()) != null && !area.upToDate) {
				areas.add(area);
			}
			if ((area = this.getRight()) != null && !area.upToDate) {
				areas.add(area);
			}
			if ((area = this.getTop()) != null && !area.upToDate) {
				areas.add(area);
			}
			if ((area = this.getBottom()) != null && !area.upToDate) {
				areas.add(area);
			}

			areas.stream().forEach(Area::initSelf);

			this.initSelf();
		}

		private void initSelf() {
			this.transitionPoints.clear();
			this.initTopLine();
			this.initBottomLine();
			this.initLeftLine();
			this.initRightLine();

			this.initConnections();

			this.upToDate = true;
			Log.PATHFINDING.info("init area with " + this.transitionPoints.size() + " transition points");
		}

		private void initConnections() {
			Area left = this.getLeft();
			Area right = this.getRight();
			Area top = this.getTop();
			Area bottom = this.getBottom();

			for (TransitionPoint transitionPoint : this.transitionPoints) {
				if (transitionPoint.startX == 0 && left != null) {
					TransitionPoint transition = left.findTransitionPoint(HierarchicalMap2.this.areaWidth - 1, transitionPoint.startY, HierarchicalMap2.this.areaWidth - 1, transitionPoint.endY);
					if (transition != null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startX == HierarchicalMap2.this.areaWidth - 1 && right != null) {
					TransitionPoint transition = right.findTransitionPoint(0, transitionPoint.startY, 0, transitionPoint.endY);
					if (transition != null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startY == 0 && top != null) {
					TransitionPoint transition = top.findTransitionPoint(transitionPoint.startX, HierarchicalMap2.this.areaHeight - 1, transitionPoint.endX, HierarchicalMap2.this.areaHeight - 1);
					if (transition != null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startY == HierarchicalMap2.this.areaHeight - 1 && bottom != null) {
					TransitionPoint transition = bottom.findTransitionPoint(transitionPoint.startX, 0, transitionPoint.endX, 0);
					if (transition != null) {
						transition.connectTo(transitionPoint);
					}
				}
			}

			Log.PATHFINDING.info("area " + this.getAreaX() + " / " + this.getAreaY() + " has connections: ");
			for (TransitionPoint transitionPoint : this.transitionPoints) {
				if (transitionPoint.partner != null) {
					Log.PATHFINDING.info(transitionPoint.partner.area.getAreaX() + " / " + transitionPoint.partner.area.getAreaY());
				}
			}

			this.initConnectionConnections();

			// TODO paths und subpaths
			// TODO pfad zurück = pfad hin nur anderst rum

		}

		private void initConnectionConnections() {
			Map<TransitionPoint, Map<TransitionPoint, Path>> transitionPointConnectionPaths = new HashMap<>();
			for (int one = 0; one < this.transitionPoints.size(); one++) {
				for (int two = 0; two < this.transitionPoints.size(); two++) {
					TransitionPoint transitionPointOne = this.transitionPoints.get(one);
					TransitionPoint transitionPointTwo = this.transitionPoints.get(two);
					if (transitionPointOne != transitionPointTwo) {
						Map<TransitionPoint, Path> innerMap = transitionPointConnectionPaths.get(transitionPointOne);
						if (innerMap == null) {
							innerMap = new HashMap<>();
							transitionPointConnectionPaths.put(transitionPointOne, innerMap);
						}
						AStar<AStarWaypoint> aStar = new AStar<AStarWaypoint>(transitionPointOne.asAStarWaypoint(), transitionPointTwo.asAStarWaypoint());
						List<AStarWaypoint> foundPath = aStar.findPath();
						if (foundPath != null) {
							Path path = new Path( foundPath.get(0),  foundPath.get(foundPath.size() - 1), foundPath);
							innerMap.put(transitionPointTwo, path);
						}
					}
				}
			}

			synchronized (this.transitionPointConnections) {
				this.transitionPointConnections.clear();
				this.transitionPointConnections.putAll(transitionPointConnectionPaths);
			}
		}

		private void initTopLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int y = 0;
			for (int x = 0; x < HierarchicalMap2.this.areaWidth; x++) {
				if (!this.isCollision(x, y) && !this.isTopCollision(x)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
			transitionPointAnalyzer.end();
		}

		private void initBottomLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int y = HierarchicalMap2.this.areaHeight - 1;
			for (int x = 0; x < HierarchicalMap2.this.areaWidth; x++) {
				if (!this.isCollision(x, y) && !this.isBottomCollision(x)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
			transitionPointAnalyzer.end();
		}

		private void initLeftLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int x = 0;
			for (int y = 0; y < HierarchicalMap2.this.areaHeight; y++) {
				if (!this.isCollision(x, y) && !this.isLeftCollision(y)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
			transitionPointAnalyzer.end();
		}

		private void initRightLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int x = HierarchicalMap2.this.areaWidth - 1;
			for (int y = 0; y < HierarchicalMap2.this.areaHeight; y++) {
				if (!this.isCollision(x, y) && !this.isRightCollision(y)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
			transitionPointAnalyzer.end();
		}

		private Area getTop() {
			return HierarchicalMap2.this.getArea(this.getAreaX(), this.getAreaY() - 1);
		}

		private Area getBottom() {
			return HierarchicalMap2.this.getArea(this.getAreaX(), this.getAreaY() + 1);
		}

		private Area getLeft() {
			return HierarchicalMap2.this.getArea(this.getAreaX() - 1, this.getAreaY());
		}

		private Area getRight() {
			return HierarchicalMap2.this.getArea(this.getAreaX() + 1, this.getAreaY());
		}

		private boolean isTopCollision(int x) {
			Area top = this.getTop();
			if (top == null) {
				return false;
			}
			return top.isCollision(x, HierarchicalMap2.this.areaHeight - 1);
		}

		private boolean isBottomCollision(int x) {
			Area bottom = this.getBottom();
			if (bottom == null) {
				return false;
			}
			return bottom.isCollision(x, 0);
		}

		private boolean isLeftCollision(int y) {
			Area left = this.getLeft();
			if (left == null) {
				return false;
			}
			return left.isCollision(HierarchicalMap2.this.areaWidth - 1, y);
		}

		private boolean isRightCollision(int y) {
			Area right = this.getRight();
			if (right == null) {
				return false;
			}
			return right.isCollision(0, y);
		}

		private int getAreaX() {
			return this.startX / HierarchicalMap2.this.areaWidth;
		}

		private int getAreaY() {
			return this.startY / HierarchicalMap2.this.areaHeight;
		}

		private boolean isCollision(int x, int y) {
			return HierarchicalMap2.this.map.getFixEntityCollisionMap().isCollision(this.startX + x, this.startY + y);
		}

		/**
		 * Checks if the given coordinates are a valid area position. Valid are
		 * all local coordinates inside the area.
		 */
		private boolean isAreaPosition(int x, int y) {
			return x >= 0 && x < HierarchicalMap2.this.areaWidth && y >= 0 && y < HierarchicalMap2.this.areaHeight;
		}

	}

	private static final int MAX_AREA_SIZE = 50;

	private transient final IF_Map map;
	private final int areaWidth;
	private final int areaHeight;
	private final Area[][] areas;

	public HierarchicalMap2(IF_Map map) {
		this.map = map;

		this.areaWidth = HierarchicalMap2.evaluateMaxAreaSize(map.getWidthInCollisionTiles());
		this.areaHeight = HierarchicalMap2.evaluateMaxAreaSize(map.getHeightInCollisionTiles());

		int widthInAreas = map.getWidthInCollisionTiles() / this.areaWidth;
		int heightInAreas = map.getHeightInCollisionTiles() / this.areaHeight;

		Log.PATHFINDING.info("Create hirarchical map with areas: " + widthInAreas + " * " + heightInAreas);

		this.areas = new Area[widthInAreas][heightInAreas];
		for (int x = 0; x < this.areas.length; x++) {
			for (int y = 0; y < this.areas[0].length; y++) {
				this.areas[x][y] = new Area(x * this.areaWidth, y * this.areaHeight);
			}
		}

		for (int x = 0; x < this.areas.length; x++) {
			for (int y = 0; y < this.areas[0].length; y++) {
				this.areas[x][y].initSelf();
			}
		}

		for (int x = 0; x < this.areas.length; x++) {
			for (int y = 0; y < this.areas.length; y++) {
				this.areas[x][y].init();
			}
		}
	}

	private static int evaluateMaxAreaSize(int size) {
		for (int i = HierarchicalMap2.MAX_AREA_SIZE; i > 0; i++) {
			if (size % i == 0) {
				return i;
			}
		}
		throw new IllegalStateException("This should never happen");
	}

	private Area getArea(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.areas.length && y < this.areas[0].length) {
			return this.areas[x][y];
		}
		return null;
	}

	private Area getAreaByCollisionTile(int x, int y) {
		return this.getArea((int) Math.floor(x / (float) this.areaWidth), (int) Math.floor(y / (float) this.areaHeight));
	}

	public Path findPath(int collisionTileStartX, int collisionTileStartY, int collisionTileEndX, int collisionTileEndY) {

		Area from = this.getAreaByCollisionTile(collisionTileStartX, collisionTileStartY);
		Area to = this.getAreaByCollisionTile(collisionTileEndX, collisionTileEndY);

		for (int i = 0; i < 10; i++) {
			Stopwatch stopwatch = new Stopwatch("find path");
			AStar<Area.TransitionPoint> aStar = new AStar<>(from.transitionPoints.get(0), to.transitionPoints.get(0));
			List<HierarchicalMap2.Area.TransitionPoint> path = aStar.findPath();

			if (path == null) {
				System.out.println("Path not possible");
			} else {
				for (Area.TransitionPoint transitionPoint : path) {
					System.out.println(transitionPoint.area.getAreaX() + " " + transitionPoint.area.getAreaY());
				}
			}
			stopwatch.stop();
		}

		// TODO

		return null;
	}

}
