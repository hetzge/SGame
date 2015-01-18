package de.hetzge.sgame.common.hierarchical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hetzge.sgame.common.Stopwatch;

public class AStar<WAYPOINT extends IF_AStarWaypoint<WAYPOINT>> {

	private class Node {

		private final WAYPOINT waypoint;
		private final List<Node> nextLevel = new LinkedList<>();
		private final List<Node> levelBefore = new LinkedList<>();

		private Node(WAYPOINT waypoint) {
			this.waypoint = waypoint;
		}

		private Node eval() {

			Map<Node, List<WAYPOINT>> nexts = new HashMap<>();
			nexts.put(this, new ArrayList<>(this.waypoint.getWaypoints(this.waypoint)));

			while (!nexts.isEmpty()) {

				Map<Node, List<WAYPOINT>> nextNexts = new HashMap<>();

				for (Map.Entry<Node, List<WAYPOINT>> entry : nexts.entrySet()) {
					Node currentNode = entry.getKey();
					List<WAYPOINT> waypoints = entry.getValue();

					for (WAYPOINT waypoint : waypoints) {
						if (!AStar.this.doneWaypoints.contains(waypoint)) {
							Node node = new Node(waypoint);
							currentNode.nextLevel.add(node);
							node.levelBefore.add(currentNode);
							AStar.this.doneWaypoints.add(waypoint);
							if (waypoint.equals(AStar.this.end)) {
								return node;
							}
							nextNexts.put(node, new ArrayList<>(node.waypoint.getWaypoints(waypoint)));
						}
					}
				}
				nexts = nextNexts;
			}

			return null;
		}
	}

	private final Set<WAYPOINT> doneWaypoints = new HashSet<>();
	private final WAYPOINT start;
	private final WAYPOINT end;
	private final Node startNode;

	public AStar(WAYPOINT start, WAYPOINT end) {
		this.start = start;
		this.end = end;
		this.startNode = new Node(start);
	}

	public List<WAYPOINT> findPath() {
		Stopwatch stopwatch = new Stopwatch("AStar#findPath");

		List<WAYPOINT> waypoints = new LinkedList<>();

		if (this.start.equals(this.end)) {
			return waypoints;
		}

		AStar<WAYPOINT>.Node endNode = this.startNode.eval();
		if (endNode == null) {
			return null;
		}

		// TODO umkehren ... start -> ziel ?!

		Node node = endNode;
		do {
			node = node.levelBefore.get(0);
			waypoints.add(node.waypoint);
		} while (!node.waypoint.equals(this.start));

		stopwatch.stop();

		return waypoints;
	}

}
