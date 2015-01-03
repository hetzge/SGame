package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

public class CommonConfig {

	public static final CommonConfig INSTANCE = new CommonConfig();

	public final FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();

	private CommonConfig() {
	}

}
