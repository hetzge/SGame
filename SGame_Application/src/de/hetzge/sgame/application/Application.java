package de.hetzge.sgame.application;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.timer.Timer;

public class Application {

	private final Timer updateTimer = new Timer(ApplicationConfig.INSTANCE.FPS);

	public Application() {
	}

	public void start() {
		this.init();
		this.postInit();
		while (true) {
			this.update();
		}
	}

	public void init() {
		ApplicationConfig.INSTANCE.modulePool.init();
	}

	public void postInit() {
		ApplicationConfig.INSTANCE.modulePool.postInit();
	}

	public void update() {
		if (this.updateTimer.isTime()) {
			ApplicationConfig.INSTANCE.modulePool.update();
		}
		Util.sleep(this.updateTimer.restTime());
	}

}
