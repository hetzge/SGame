package de.hetzge.sgame.common.application;

import se.jbee.inject.Injector;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.FPS;
import de.hetzge.sgame.common.IF_DependencyInjection;
import de.hetzge.sgame.common.ModulePool;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.timer.Timer;

public abstract class Application implements IF_DependencyInjection {

	public static Injector INJECTOR;
	protected final ModulePool modulePool;
	private final Timer updateTimer = new Timer(ApplicationConfig.INSTANCE.FPS);

	public Application(Class<? extends BootstrapperBundle> bootstrapperBundle) {
		Application.INJECTOR = Bootstrap.injector(bootstrapperBundle);
		this.modulePool = this.get(ModulePool.class);
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
		if (this.updateTimer.isTime()) {
			this.modulePool.update();
		}
		Util.sleep(this.updateTimer.restTime());
	}

}
