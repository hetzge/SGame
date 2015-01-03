package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.message.IF_MessageHandler;

public class RemoveEntityMessageHandler implements IF_MessageHandler<RemoveEntityMessage> {

	@Override
	public void handle(RemoveEntityMessage message) {
		EntityConfig.INSTANCE.entityPool.removeEntity(message.entity);

	}

}
