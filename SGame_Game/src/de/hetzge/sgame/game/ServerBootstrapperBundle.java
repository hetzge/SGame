package de.hetzge.sgame.game;

import de.hetzge.sgame.entity.ki.KIBinderModule;

public class ServerBootstrapperBundle extends BaseGameBootstrapperBundle {

	@Override
	protected void bootstrap() {
		super.bootstrap();
		this.install(KIBinderModule.class);
	}

}
