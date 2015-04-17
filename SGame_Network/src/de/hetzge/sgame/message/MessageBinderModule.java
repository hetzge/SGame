package de.hetzge.sgame.message;

import se.jbee.inject.bind.BinderModule;

public class MessageBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(MessageModule.class).to(MessageModule.class);
		this.bind(MessageConfig.class).to(MessageConfig.class);
		this.bind(MessagePool.class).to(MessagePool.class);
		this.bind(MessageHandlerPool.class).to(MessageHandlerPool.class);
		this.bind(BatchMessageHandler.class).to(BatchMessageHandler.class);
	}

}
