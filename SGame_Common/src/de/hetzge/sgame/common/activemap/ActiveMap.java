package de.hetzge.sgame.common.activemap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javolution.util.FastCollection;
import javolution.util.FastSet;
import de.hetzge.sgame.common.hierarchical.XY;

public class ActiveMap<TYPE> implements Serializable {

	private class ActiveNode<TYPE> implements Serializable {

		private transient FastCollection<ActiveNode<TYPE>> connectors = new FastSet<ActiveNode<TYPE>>().parallel();
		private transient ActiveNode<TYPE> connectedWith;

		private final int x;
		private final int y;

		private TYPE object;

		public ActiveNode(int x, int y) {
			this(x, y, null);
		}

		public ActiveNode(int x, int y, TYPE object) {
			this.object = object;
			this.x = x;
			this.y = y;
		}

		public void connectTo(ActiveNode<TYPE> activeNode) {
			if (activeNode != this.connectedWith) {
				if (this.connectedWith != null) {
					this.connectedWith.removeConnection(this);
				}
				activeNode.getLazyConnectors().add(this);
				this.connectedWith = activeNode;
			}
		}

		private void removeConnection(ActiveNode<TYPE> activeNode) {
			this.getLazyConnectors().remove(activeNode);
			if (this.getLazyConnectors().isEmpty()) {
				ActiveMap.this.nodesByXY.remove(new XY(this.x, this.y));
			}
		}

		public TYPE getObject() {
			return this.object;
		}

		public FastCollection<TYPE> getConnectedObjects() {
			FastCollection<TYPE> result = new FastSet<TYPE>().parallel();
			for (ActiveNode<TYPE> activeNode : this.getLazyConnectors()) {
				result.add(activeNode.getObject());
			}
			return result;
		}

		/**
		 * method is needed because after serialization the connectors set is
		 * null
		 */
		public FastCollection<ActiveNode<TYPE>> getLazyConnectors() {
			if (this.connectors == null) {
				this.connectors = new FastSet<ActiveNode<TYPE>>().parallel();
			}
			return this.connectors;
		}
	}

	private final int widthInTiles;
	private final int heightInTiles;

	// private final ActiveNode<TYPE>[][] nodes;

	private final Map<XY, ActiveNode<TYPE>> nodesByXY = new HashMap<>();

	public ActiveMap(int widthInTiles, int heightInTiles) {
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
	}

	/**
	 * connects the activeMap to the current one at the given position
	 */
	public void connect(int startX, int startY, ActiveMap<TYPE> activeMap) {
		if (startX < 0) {
			startX = 0;
		}
		if (startY < 0) {
			startY = 0;
		}

		int endX = startX + activeMap.getWidthInTiles();
		int endY = startY + activeMap.getHeightInTiles();

		if (endX > this.getWidthInTiles()) {
			endX = this.getWidthInTiles();
		}
		if (endY > this.getHeightInTiles()) {
			endY = this.getHeightInTiles();
		}

		int x2 = 0;
		for (int x = startX; x < endX; x++) {
			int y2 = 0;
			for (int y = startY; y < endY; y++) {
				activeMap.getActiveNode(x2, y2).connectTo(this.getActiveNode(x, y));
				y2++;
			}
			x2++;
		}
	}

	private ActiveNode<TYPE> getActiveNode(int x, int y) {
		XY xy = new XY(x, y);
		ActiveNode<TYPE> activeNode = this.nodesByXY.get(xy);
		if (activeNode == null) {
			activeNode = new ActiveNode<>(x, y);
			this.nodesByXY.put(xy, activeNode);
		}
		return activeNode;
	}

	public ActiveMap<TYPE> setObject(int x, int y, TYPE object) {
		this.getActiveNode(x, y).object = object;
		return this;
	}

	public ActiveMap<TYPE> setObject(TYPE object) {
		for (int x = 0; x < this.widthInTiles; x++) {
			for (int y = 0; y < this.heightInTiles; y++) {
				this.setObject(x, y, object);
			}
		}
		return this;
	}

	public Collection<TYPE> getConnectedObjects(int x, int y) {
		if (this.nodesByXY.get(new XY(x, y)) == null) {
			return Collections.emptyList();
		}
		return this.getActiveNode(x, y).getConnectedObjects();
	}

	public TYPE getNodeObject(int x, int y) {
		if (this.nodesByXY.get(new XY(x, y)) == null) {
			return null;
		}
		return this.getActiveNode(x, y).getObject();
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
		Collection<Boolean> connectedObjects = activeMap.getConnectedObjects(4, 4);
		for (Boolean boolean1 : connectedObjects) {
			System.out.println(boolean1);
		}

		activeMap.connect(2, 2, activeMap2);
		Collection<Boolean> connectedObjects2 = activeMap.getConnectedObjects(4, 4);
		for (Boolean boolean1 : connectedObjects2) {
			System.out.println(boolean1);
		}
	}
}
