package de.hetzge.sgame.message;

public class BatchMessageHandler implements IF_MessageHandler<BatchMessage> {

	private final MessageHandlerPool messageHandlerPool;

	public BatchMessageHandler(MessageHandlerPool messageHandlerPool) {
		this.messageHandlerPool = messageHandlerPool;
	}

	@Override
	public void handle(BatchMessage message) {
		for (Object object : message.getMessages()) {
			this.messageHandlerPool.handleMessage(object);
		}
	}

}
