package de.hetzge.sgame.game;

import de.hetzge.sgame.entity.EntityBinderModule;


public class ServerBootstrapperBundle extends BaseGameBootstrapperBundle {

	@Override
	protected void bootstrap() {
		super.bootstrap();
		this.install(EntityBinderModule.ServerBinderModule.class);
	}

}
