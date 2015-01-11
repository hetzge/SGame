package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.common.PathfinderThread;

public class KIConfig {

	public static final KIConfig INSTANCE = new KIConfig();

	public final PathfinderThread pathfinderThread = new PathfinderThread();

	private KIConfig() {
	}

}
