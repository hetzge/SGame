package de.hetzge.sgame.application;

public final class ApplicationConfig {

	public static final ApplicationConfig INSTANCE = new ApplicationConfig();

	public final int FPS = 30;
	public final ModulePool modulePool = new ModulePool();

	static {
	}

	private ApplicationConfig() {
	}
}
