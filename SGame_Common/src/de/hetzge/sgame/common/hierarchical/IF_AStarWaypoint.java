package de.hetzge.sgame.common.hierarchical;

import java.util.List;

public interface IF_AStarWaypoint<WAYPOINT extends IF_AStarWaypoint<?>> {

	// TODO macht das reingeben als Paramter noch Sinn ?
	List<WAYPOINT> getWaypoints(WAYPOINT waypoint);

}
