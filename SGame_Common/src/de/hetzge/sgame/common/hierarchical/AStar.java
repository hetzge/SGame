package de.hetzge.sgame.common.hierarchical;

import java.util.LinkedList;
import java.util.List;

import javolution.util.FastSet;

public class AStar<WAYPOINT extends IF_AStarWaypoint<WAYPOINT>> {

	private class Node {

		private final WAYPOINT waypoint;
		private final List<Node> nextLevel = new LinkedList<>();
		private final List<Node> levelBefore = new LinkedList<>();

		private Node(WAYPOINT waypoint) {
			this.waypoint = waypoint;
		}

		private Node eval() {
			List<WAYPOINT> waypoints = waypoint.getWaypoints();
			for (WAYPOINT waypoint : waypoints) {
				if (!doneWaypoints.contains(waypoint)) {
					Node node = new Node(waypoint);
					nextLevel.add(node);
					node.levelBefore.add(this);
					doneWaypoints.add(waypoint);
					if (waypoint.equals(end)) {
						return node;
					}
				}
			}
			for (Node next : nextLevel) {
				Node endNode = next.eval();
				if(endNode != null){
					return endNode;
				}
			}
			return null;
		}
	}

	private final FastSet<WAYPOINT> doneWaypoints = new FastSet<>();
	private final WAYPOINT start;
	private final WAYPOINT end;
	private final Node startNode;

	public AStar(WAYPOINT start, WAYPOINT end) {
		this.start = start;
		this.end = end;
		this.startNode = new Node(start);
	}

	public List<WAYPOINT> findPath() {
		List<WAYPOINT> waypoints = new LinkedList<>();
		
		if (start.equals(end)) {
			return waypoints;
		}

		AStar<WAYPOINT>.Node endNode = startNode.eval();
		if (endNode == null) {
			return null;
		}

		Node node = endNode;
		do {
			node = node.levelBefore.get(0);
			waypoints.add(node.waypoint);
		} while (node.waypoint != start);
		
		return waypoints;
	}

}
