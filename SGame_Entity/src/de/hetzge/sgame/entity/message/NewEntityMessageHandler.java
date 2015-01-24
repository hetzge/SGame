package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.message.IF_MessageHandler;

public class NewEntityMessageHandler implements IF_MessageHandler<NewEntityMessage> {

	private final EntityPool entityPool;

	public NewEntityMessageHandler(EntityPool entityPool) {
		this.entityPool = entityPool;
	}

	@Override
	public void handle(NewEntityMessage message) {
		this.entityPool.addEntity(message.entity);
	}
}
