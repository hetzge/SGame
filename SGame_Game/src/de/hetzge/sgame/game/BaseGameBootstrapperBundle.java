package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.CommonBinderModule;
import de.hetzge.sgame.entity.EntityBinderModule;
import de.hetzge.sgame.map.MapBinderModule;
import de.hetzge.sgame.message.MessageBinderModule;
import de.hetzge.sgame.sync.SyncBinderModule;

public class BaseGameBootstrapperBundle extends BootstrapperBundle {

	@Override
	protected void bootstrap() {
		this.install(EntityBinderModule.class);
		this.install(CommonBinderModule.class);
		this.install(MapBinderModule.class);
		this.install(SyncBinderModule.class);
		this.install(MessageBinderModule.class);
	}

}
