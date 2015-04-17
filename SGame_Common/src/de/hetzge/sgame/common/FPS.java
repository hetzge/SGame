package de.hetzge.sgame.common;

import de.hetzge.sgame.common.application.ApplicationConfig;

public class FPS {

	private static final int FRAMES_PER_SECOND = ApplicationConfig.INSTANCE.FPS;
	private static long lastCycleTimestamp = System.currentTimeMillis();
	private static float ticks = 0;

	public static float ticks() {
		return FPS.ticks;
	}

	public static void update() {
		long currentTimeInMilliseconds = System.currentTimeMillis();
		long dif = currentTimeInMilliseconds - FPS.lastCycleTimestamp;
		FPS.ticks = dif / (1000f / FPS.FRAMES_PER_SECOND);
		FPS.lastCycleTimestamp = currentTimeInMilliseconds;
	}

}
