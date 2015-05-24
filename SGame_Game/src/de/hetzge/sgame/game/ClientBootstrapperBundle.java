package de.hetzge.sgame.game;

import de.hetzge.sgame.entity.EntityBinderModule;


public class ClientBootstrapperBundle extends BaseGameBootstrapperBundle {

	@Override
	protected void bootstrap() {
		super.bootstrap();
		this.install(EntityBinderModule.ClientBinderModule.class);
	}

}
