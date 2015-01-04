package de.hetzge.sgame.common.activemap;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ActiveMap<TYPE> implements Serializable {

	private class ActiveNode<TYPE> implements Serializable {

		private transient Set<ActiveNode> connectors = new HashSet<>();
		private transient ActiveNode connectedWith;

		private TYPE object;

		public ActiveNode() {
			this.object = null;
		}

		public ActiveNode(TYPE object) {
			this.object = object;
		}

		public void connectTo(ActiveNode activeNode) {
			if (this.connectedWith != null) {
				this.connectedWith.getLazyConnectors().remove(this);
			}
			activeNode.getLazyConnectors().add(this);
			this.connectedWith = activeNode;
		}

		public TYPE getObject() {
			return this.object;
		}

		public Set<TYPE> getConnectedObjects() {
			Set<TYPE> result = new HashSet<>();
			for (ActiveNode<TYPE> activeNode : this.getLazyConnectors()) {
				result.add(activeNode.getObject());
			}
			return result;
		}

		/**
		 * method is needed because after serialization the connectors set is
		 * null
		 */
		public Set<ActiveNode> getLazyConnectors() {
			if (this.connectors == null) {
				this.connectors = new HashSet<>();
			}
			return this.connectors;
		}
	}

	private final int widthInTiles;
	private final int heightInTiles;
	private final ActiveNode<TYPE>[][] nodes;

	public ActiveMap(int widthInTiles, int heightInTiles) {
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;

		this.nodes = new ActiveNode[widthInTiles][];

		for (int x = 0; x < widthInTiles; x++) {
			this.nodes[x] = new ActiveNode[heightInTiles];
			for (int y = 0; y < heightInTiles; y++) {
				this.nodes[x][y] = new ActiveNode<>();
			}
		}
	}

	/**
	 * connects the activeMap to the current one at the given position
	 */
	public void connect(int startX, int startY, ActiveMap activeMap) {
		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;

		int endX = startX + activeMap.getWidthInTiles();
		int endY = startY + activeMap.getHeightInTiles();

		if (endX > this.getWidthInTiles())
			endX = this.getWidthInTiles();
		if (endY > this.getHeightInTiles())
			endY = this.getHeightInTiles();

		int x2 = 0;
		for (int x = startX; x < endX; x++) {
			int y2 = 0;
			for (int y = startY; y < endY; y++) {
				activeMap.nodes[x2][y2].connectTo(this.nodes[x][y]);
				y2++;
			}
			x2++;
		}
	}

	public void setObject(int x, int y, TYPE object) {
		this.nodes[x][y].object = object;
	}

	public void setObject(TYPE object) {
		for (int x = 0; x < this.widthInTiles; x++) {
			for (int y = 0; y < this.heightInTiles; y++) {
				this.nodes[x][y].object = object;
			}
		}
	}

	public Set<TYPE> getConnectedObjects(int x, int y) {
		return this.nodes[x][y].getConnectedObjects();
	}

	public int getWidthInTiles() {
		return this.widthInTiles;
	}

	public int getHeightInTiles() {
		return this.heightInTiles;
	}

	public static void main(String[] args) {
		ActiveMap<Boolean> activeMap = new ActiveMap<>(100, 100);
		ActiveMap<Boolean> activeMap2 = new ActiveMap<>(3, 3);
		activeMap2.setObject(true);

		activeMap.connect(3, 3, activeMap2);
		Set<Boolean> connectedObjects = activeMap.getConnectedObjects(4, 4);
		for (Boolean boolean1 : connectedObjects) {
			System.out.println(boolean1);
		}

		activeMap.connect(2, 2, activeMap2);
		Set<Boolean> connectedObjects2 = activeMap.getConnectedObjects(4, 4);
		for (Boolean boolean1 : connectedObjects2) {
			System.out.println(boolean1);
		}
	}
}