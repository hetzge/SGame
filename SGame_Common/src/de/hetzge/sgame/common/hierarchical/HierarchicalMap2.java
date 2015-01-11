package de.hetzge.sgame.common.hierarchical;

import javolution.util.FastTable;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.definition.IF_Map;

public class HierarchicalMap2 {

	private class Area {

		private class TransitionPointAnalyzer {
			private int startX, endX, startY, endY = -1;

			public void add(int x, int y) {
				if (this.startX == -1) {
					this.startX = x;
					this.startY = y;
				} else {
					this.endX = x;
					this.endY = y;
				}
			}

			public void end() {
				if (this.startX != -1) {
					if (this.endX == -1) {
						this.endX = this.startX;
						this.endY = this.startY;
					}
					Area.this.transitionPoints.add(new TransitionPoint(this.startX, this.endX, this.startY, this.endY));
					this.startX = this.endX = this.startY = this.endY = -1;
				}
			}
		}

		private class TransitionPoint {
			private final int startX;
			private final int endX;
			private final int startY;
			private final int endY;

			// private final Area toArea;
			// private final int toGroup;

			public TransitionPoint(int startX, int endX, int startY, int endY) {
				this.startX = startX;
				this.endX = endX;
				this.startY = startY;
				this.endY = endY;
			}
		}

		private class TransitionPointConnection {
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

		public Area(int startX, int startY) {
			this.startX = startX;
			this.startY = startY;
		}

		private void init() {
			this.transitionPoints.clear();
			this.initTopLine();
			this.initBottomLine();
			this.initLeftLine();
			this.initRightLine();
			this.initConnections();
		}

		private void initConnections() {

		}

		private void initTopLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int y = this.startY - 1;
			for (int x = this.startX; x < this.startX + HierarchicalMap2.this.areaWidth - 1; x++) {
				if (!this.isCollision(x, y) && !this.isTopCollision(x)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
		}

		private void initBottomLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int y = this.startY + HierarchicalMap2.this.areaHeight - 1;
			for (int x = this.startX; x < this.startX + HierarchicalMap2.this.areaWidth - 1; x++) {
				if (!this.isCollision(x, y) && !this.isBottomCollision(x)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
		}

		private void initLeftLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int x = this.startX - 1;
			for (int y = this.startY; y < this.startY + HierarchicalMap2.this.areaHeight - 1; y++) {
				if (!this.isCollision(x, y) && !this.isLeftCollision(y)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
		}

		private void initRightLine() {
			TransitionPointAnalyzer transitionPointAnalyzer = new TransitionPointAnalyzer();

			int x = this.startX + HierarchicalMap2.this.areaWidth - 1;
			for (int y = this.startY; y < this.startY + HierarchicalMap2.this.areaHeight - 1; y++) {
				if (!this.isCollision(x, y) && !this.isRightCollision(y)) {
					transitionPointAnalyzer.add(x, y);
				} else {
					transitionPointAnalyzer.end();
				}
			}
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
			return top.isCollision(x, top.startY + HierarchicalMap2.this.areaHeight - 1);
		}

		private boolean isBottomCollision(int x) {
			Area bottom = this.getBottom();
			if (bottom == null) {
				return false;
			}
			return bottom.isCollision(x, bottom.startY);
		}

		private boolean isLeftCollision(int y) {
			Area left = this.getLeft();
			if (left == null) {
				return false;
			}
			return left.isCollision(left.startX + HierarchicalMap2.this.areaWidth - 1, y);
		}

		private boolean isRightCollision(int y) {
			Area right = this.getLeft();
			if (right == null) {
				return false;
			}
			return right.isCollision(right.startX, y);
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

		this.areas = new Area[map.getWidthInCollisionTiles() / this.areaWidth][map.getHeightInCollisionTiles() / this.areaHeight];
		for (int x = 0; x < this.areas.length; x++) {
			for (int y = 0; y < this.areas[0].length; y++) {
				this.areas[x][y] = new Area(x * this.areaWidth, y * this.areaHeight);
			}
		}

		for (int x = 0; x < this.areas.length; x++) {
			for (int y = 0; y < this.areas[0].length; y++) {
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
		if (x > 0 && y > 0 && x < this.areas.length && y < this.areas[0].length) {
			return this.areas[x][y];
		}
		return null;
	}

}
