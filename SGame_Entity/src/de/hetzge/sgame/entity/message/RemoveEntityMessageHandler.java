package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.message.IF_MessageHandler;

public class RemoveEntityMessageHandler implements IF_MessageHandler<RemoveEntityMessage> {

	private final EntityPool entityPool;

	public RemoveEntityMessageHandler(EntityPool entityPool) {
		this.entityPool = entityPool;
	}

	@Override
	public void handle(RemoveEntityMessage message) {
		System.out.println("remove entity " + message.entity.getId());
		this.entityPool.removeEntity(message.entity);
	}

}
