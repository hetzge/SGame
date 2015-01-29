package de.hetzge.sgame.common;

import se.jbee.inject.util.Provider;
import de.hetzge.sgame.common.definition.IF_Map;

public interface IF_MapProvider extends Provider<IF_Map> {

	void setMap(IF_Map map);

}
