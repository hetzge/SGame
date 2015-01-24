package de.hetzge.sgame.common.application;

public final class ApplicationConfig {

	public static final ApplicationConfig INSTANCE = new ApplicationConfig();

	public final int FPS = 30;

	static {
	}

	private ApplicationConfig() {
	}
}
