package de.hetzge.sgame.message;

import de.hetzge.sgame.common.definition.IF_Module;

public class MessageModule implements IF_Module {

	@Override
	public void init() {
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(BatchMessage.class, new BatchMessageHandler());
	}

	@Override
	public void postInit() {
	}

	@Override
	public void update() {
	}

}
