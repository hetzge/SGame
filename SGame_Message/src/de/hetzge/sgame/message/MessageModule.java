package de.hetzge.sgame.message;

import de.hetzge.sgame.common.definition.IF_Module;

public class MessageModule implements IF_Module {

	private final MessageHandlerPool messageHandlerPool;

	public MessageModule(MessageHandlerPool messageHandlerPool) {
		this.messageHandlerPool = messageHandlerPool;
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(BatchMessage.class, this.get(BatchMessageHandler.class));
	}

	@Override
	public void postInit() {
	}

	@Override
	public void update() {
	}

}
