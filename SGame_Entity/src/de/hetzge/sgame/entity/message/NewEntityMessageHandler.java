package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.message.IF_MessageHandler;

public class NewEntityMessageHandler implements IF_MessageHandler<NewEntityMessage> {

	@Override
	public void handle(NewEntityMessage message) {
		EntityConfig.INSTANCE.entityPool.addEntity(message.entity);
	}
}
