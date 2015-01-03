package de.hetzge.sgame.message;

public class BatchMessageHandler implements IF_MessageHandler<BatchMessage> {

	@Override
	public void handle(BatchMessage message) {
		for (Object object : message.getMessages()) {
			MessageConfig.INSTANCE.messageHandlerPool.handleMessage(object);
		}
	}

}
