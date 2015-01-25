package de.hetzge.sgame.sync;

import se.jbee.inject.bind.BinderModule;
import de.hetzge.sgame.sync.message.SyncMessageHandler;

public class SyncBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(SyncModule.class).to(SyncModule.class);
		this.bind(SyncPool.class).to(SyncPool.class);
		this.bind(SyncConfig.class).to(SyncConfig.class);
		this.bind(SyncMessageHandler.class).to(SyncMessageHandler.class);
	}

}
