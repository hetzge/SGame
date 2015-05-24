package de.hetzge.sgame.common.application;

import se.jbee.inject.Injector;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.FPS;
import de.hetzge.sgame.common.IF_DependencyInjection;
import de.hetzge.sgame.common.timer.Timer;

public abstract class Application implements IF_DependencyInjection {

	public static Injector INJECTOR;
	protected final ModulePool modulePool;

	private final boolean server;
	private final boolean client;
	private final boolean singleplayer;

	private final Timer updateTimer = new Timer(1000 / ApplicationConfig.INSTANCE.FPS);

	public Application(Class<? extends BootstrapperBundle> bootstrapperBundle, boolean singleplayer, boolean server, boolean client) {
		Application.INJECTOR = Bootstrap.injector(bootstrapperBundle);
		this.modulePool = this.get(ModulePool.class);
		this.singleplayer = singleplayer;
		this.server = server;
		this.client = client;
	}

	public void start() {
		this.init();
		this.postInit();
		while (true) {
			this.update();
		}
	}

	public void init() {
		this.modulePool.init();
	}

	public void postInit() {
		this.modulePool.postInit();
	}

	public void update() {
		FPS.update();
		// if (this.updateTimer.isTime()) {
		this.modulePool.update();
		// }
		// Util.sleep(this.updateTimer.restTime());
	}

	public boolean isServer() {
		return this.server;
	}

	public boolean isClient() {
		return this.client;
	}

	public boolean isSingleplayer() {
		return this.singleplayer;
	}

}
