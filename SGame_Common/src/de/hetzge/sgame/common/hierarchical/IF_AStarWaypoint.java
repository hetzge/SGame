package de.hetzge.sgame.common.hierarchical;

import java.util.List;

public interface IF_AStarWaypoint<WAYPOINT extends IF_AStarWaypoint<?>> {

	List<WAYPOINT> getWaypoints();
	
}
