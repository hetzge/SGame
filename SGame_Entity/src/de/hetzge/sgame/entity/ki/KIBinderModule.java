package de.hetzge.sgame.entity.ki;

import se.jbee.inject.bind.BinderModule;

public class KIBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(KIModule.class).to(KIModule.class);
		this.bind(KIConfig.class).to(KIConfig.class);
	}

}
