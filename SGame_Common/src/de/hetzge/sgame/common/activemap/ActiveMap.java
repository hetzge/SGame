package de.hetzge.sgame.common.activemap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastSet;
import de.hetzge.sgame.common.newgeometry2.IF_Coordinate_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

public class ActiveMap<TYPE> implements Serializable {

	private class ActiveNode implements Serializable {

		private transient FastCollection<ActiveNode> connectors = new FastSet<ActiveNode>().shared();
		private transient ActiveNode connectedWith;

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

		public void connectTo(ActiveNode activeNode) {
			if (activeNode != this.connectedWith) {
				if (this.connectedWith != null) {
					this.connectedWith.removeConnection(this);
				}
				activeNode.getLazyConnectors().add(this);
				this.connectedWith = activeNode;
			}
		}

		private void unchain() {
			if (this.connectedWith != null) {
				this.connectedWith.removeConnection(this);
			}
			FastCollection<ActiveNode> connectors = this.getLazyConnectors();
			for (ActiveNode activeNode : connectors) {
				activeNode.connectedWith = null;
			}
			if (this.getLazyConnectors().isEmpty()) {
				ActiveMap.this.nodesByXY.remove(new XY(this.x, this.y));
			}
		}

		private void removeConnection(ActiveNode activeNode) {
			this.getLazyConnectors().remove(activeNode);
			if (this.getLazyConnectors().isEmpty()) {
				ActiveMap.this.nodesByXY.remove(new XY(this.x, this.y));
			}
		}

		public TYPE getObject() {
			return this.object;
		}

		public FastCollection<TYPE> getConnectedObjects() {
			FastCollection<TYPE> result = new FastSet<TYPE>().shared();
			for (ActiveNode activeNode : this.getLazyConnectors()) {
				result.add(activeNode.getObject());
			}
			return result;
		}

		/**
		 * method is needed because after serialization the connectors set is
		 * null
		 */
		public FastCollection<ActiveNode> getLazyConnectors() {
			if (this.connectors == null) {
				this.connectors = new FastSet<ActiveNode>().shared();
			}
			return this.connectors;
		}
	}

	private final Map<IF_Coordinate_Immutable, ActiveNode> nodesByXY = new FastMap<IF_Coordinate_Immutable, ActiveNode>().shared();

	public ActiveMap() {
	}

	public ActiveMap(TYPE object) {
		this.setObjectOnPosition(object, 0, 0);
	}

	/**
	 * connects the activeMap to the current one at the given position
	 */
	public void connect(int startX, int startY, ActiveMap<TYPE> activeMap) {
		for (Map.Entry<IF_Coordinate_Immutable, ActiveNode> entry : activeMap.nodesByXY.entrySet()) {
			activeMap.getActiveNode(entry.getKey().getColumn(), entry.getKey().getRow()).connectTo(this.getActiveNode(startX + entry.getKey().getColumn(), startY + entry.getKey().getRow()));
		}
	}

	private ActiveNode getActiveNode(int x, int y) {
		XY xy = new XY(x, y);
		ActiveNode activeNode = this.nodesByXY.get(xy);
		if (activeNode == null) {
			activeNode = new ActiveNode(x, y);
			this.nodesByXY.put(xy, activeNode);
		}
		return activeNode;
	}

	public ActiveMap<TYPE> setObjectOnPosition(TYPE object, int x, int y) {
		this.getActiveNode(x, y).object = object;
		return this;
	}

	public ActiveMap<TYPE> setObjectInArea(TYPE object, int width, int height) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.setObjectOnPosition(object, x, y);
			}
		}
		return this;
	}

	public void unsetObjects() {
		this.setObjectInArea(null, 0, 0);
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

	public void unchain() {
		Collection<ActiveNode> activeNodes = this.nodesByXY.values();
		for (ActiveNode activeNode : activeNodes) {
			activeNode.unchain();
		}
	}

	public static void main(String[] args) {
		ActiveMap<Boolean> activeMap = new ActiveMap<>();
		ActiveMap<Boolean> activeMap2 = new ActiveMap<>();
		activeMap2.setObjectInArea(true, 3, 3);

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
