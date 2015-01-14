package de.hetzge.sgame.common.hierarchical;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javolution.util.FastTable;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.definition.IF_Map;

public class HierarchicalMap2 implements Serializable {

	private class Area implements Serializable {

		private class TransitionPointAnalyzer {
			private int startX, endX, startY, endY;

			public TransitionPointAnalyzer() {
				reset();
			}

			private void reset() {
				startX = endX = startY = endY = -1;
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
					reset();
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

			private void connectTo(TransitionPoint to) {
				partner = to;
				to.partner = this;
			}

			private boolean isPositionIn(int x, int y) {
				return startX <= x && startY <= y && endX >= x && endY >= y;
			}

			private boolean match(int startX, int startY, int endX, int endY) {
				return this.startX == startX && this.startY == startY && this.endX == endX && this.endY == endY;
			}

			@Override
			public List<TransitionPoint> getWaypoints() {
				List<TransitionPoint> transitionPoints = new LinkedList<>();
				if (partner != null) {
					transitionPoints = partner.area.transitionPoints;
				}
				return transitionPoints;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result + endX;
				result = prime * result + endY;
				result = prime * result + startX;
				result = prime * result + startY;
				if (partner != null) {
					result = prime * result + partner.startX;
					result = prime * result + partner.startY;
					result = prime * result + partner.endX;
					result = prime * result + partner.endY;
				}
				result = prime * result + area.getAreaX();
				result = prime * result + area.getAreaY();
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				TransitionPoint other = (TransitionPoint) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (endX != other.endX)
					return false;
				if (endY != other.endY)
					return false;
				if (startX != other.startX)
					return false;
				if (startY != other.startY)
					return false;
				if (partner == null && other.partner != partner)
					return false;

				if (partner != null) {
					if (partner.startX != other.partner.startX)
						return false;
					if (partner.startY != other.partner.startY)
						return false;
					if (partner.endX != other.partner.endX)
						return false;
					if (partner.endY != other.partner.endY)
						return false;
				}

				if (area.getAreaX() != other.area.getAreaX())
					return false;
				if (area.getAreaY() != other.area.getAreaY())
					return false;

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
		private final FastTable<TransitionPoint> transitionPoints = new FastTable<TransitionPoint>();
		private final boolean upToDate = false;

		public Area(int startX, int startY) {
			this.startX = startX;
			this.startY = startY;
		}

		private TransitionPoint findTransitionPoint(int startX, int startY, int endX, int endY) {
			for (TransitionPoint transitionPoint : transitionPoints) {
				if (transitionPoint.match(startX, startY, endX, endY)) {
					return transitionPoint;
				}
			}
			return null;
		}

		private void init() {
			initSelf();

			Area area;
			if ((area = getLeft()) != null && !area.upToDate)
				area.initSelf();
			if ((area = getRight()) != null && !area.upToDate)
				area.initSelf();
			if ((area = getTop()) != null && !area.upToDate)
				area.initSelf();
			if ((area = getBottom()) != null && !area.upToDate)
				area.initSelf();

			initConnections();
		}

		private void initSelf() {
			this.transitionPoints.clear();
			this.initTopLine();
			this.initBottomLine();
			this.initLeftLine();
			this.initRightLine();

			Log.PATHFINDING.info("init area with " + transitionPoints.size() + " transition points");
		}

		private void initConnections() {
			Area left = getLeft();
			Area right = getRight();
			Area top = getTop();
			Area bottom = getBottom();

			for (TransitionPoint transitionPoint : transitionPoints) {
				if (transitionPoint.startX == 0 && left != null) {
					TransitionPoint transition = left.findTransitionPoint(areaWidth - 1, transitionPoint.startY, areaWidth - 1, transitionPoint.endY);
					if (transition != null && transition.partner == null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startX == areaWidth - 1 && right != null) {
					TransitionPoint transition = right.findTransitionPoint(0, transitionPoint.startY, 0, transitionPoint.endY);
					if (transition != null && transition.partner == null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startY == 0 && top != null) {
					TransitionPoint transition = top.findTransitionPoint(transitionPoint.startX, areaHeight - 1, transitionPoint.endX, areaHeight - 1);
					if (transition != null && transition.partner == null) {
						transition.connectTo(transitionPoint);
					}
				}
				if (transitionPoint.startY == areaHeight - 1 && bottom != null) {
					TransitionPoint transition = bottom.findTransitionPoint(transitionPoint.startX, 0, transitionPoint.endX, 0);
					if (transition != null && transition.partner == null) {
						transition.connectTo(transitionPoint);
					}
				}
			}

			Log.PATHFINDING.info("area " + getAreaX() + " / " + getAreaY() + " has connections: ");
			for (TransitionPoint transitionPoint : transitionPoints) {
				if (transitionPoint.partner != null) {
					Log.PATHFINDING.info(transitionPoint.partner.area.getAreaX() + " / " + transitionPoint.partner.area.getAreaY());
				}
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

	}

	private static final int MAX_AREA_SIZE = 20;

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

		for (int x = 0; x < areas.length; x++) {
			for (int y = 0; y < areas.length; y++) {
				this.areas[x][y].init();
			}
		}
	}

	private static int evaluateMaxAreaSize(int size) {
		for (int i = HierarchicalMap2.MAX_AREA_SIZE; i > 0; i++) {
			if (size % i == 0)
				return i;
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
		return getArea((int) Math.floor(x / (float) areaWidth), (int) Math.floor(y / (float) areaHeight));
	}

	public Path findPath(int collisionTileStartX, int collisionTileStartY, int collisionTileEndX, int collisionTileEndY) {

		Area from = getAreaByCollisionTile(collisionTileStartX, collisionTileStartY);
		Area to = getAreaByCollisionTile(collisionTileEndX, collisionTileEndY);

		AStar<Area.TransitionPoint> aStar = new AStar<>(from.transitionPoints.get(0), to.transitionPoints.get(0));
		List<HierarchicalMap2.Area.TransitionPoint> path = aStar.findPath();

		for (Area.TransitionPoint transitionPoint : path) {
			System.out.println(transitionPoint.area.getAreaX() + " " + transitionPoint.area.getAreaY());
		}

		return null;
	}

}
