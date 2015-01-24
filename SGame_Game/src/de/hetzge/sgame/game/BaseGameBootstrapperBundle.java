package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.CommonBinderModule;
import de.hetzge.sgame.entity.EntityBinderModule;

public class BaseGameBootstrapperBundle extends BootstrapperBundle {

	@Override
	protected void bootstrap() {
		this.install(EntityBinderModule.class);
		this.install(CommonBinderModule.class);
	}

}
