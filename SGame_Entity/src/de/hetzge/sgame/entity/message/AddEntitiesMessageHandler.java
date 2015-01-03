package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.message.IF_MessageHandler;

public class AddEntitiesMessageHandler implements IF_MessageHandler<AddEntitiesMessage> {

	@Override
	public void handle(AddEntitiesMessage message) {
		System.out.println("HANDLE " + message.entities.size());
		for (Entity entity : message.entities) {
			EntityConfig.INSTANCE.entityPool.addEntity(entity);
		}
	}
}
