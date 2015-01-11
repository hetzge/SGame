package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.definition.IF_Map;

public class CommonConfig {

	public static final CommonConfig INSTANCE = new CommonConfig();

	public final FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();

	public IF_Map map = new DummyMap();

	private CommonConfig() {
	}

}
