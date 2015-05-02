package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.ki.ClientEntityKI;
import de.hetzge.sgame.message.IF_MessageHandler;

public class AddEntitiesMessageHandler implements IF_MessageHandler<AddEntitiesMessage> {

	private final EntityPool entityPool;

	public AddEntitiesMessageHandler(EntityPool entityPool) {
		this.entityPool = entityPool;
	}

	@Override
	public void handle(AddEntitiesMessage message) {
		System.out.println("HANDLE " + message.entities.size());
		for (Entity entity : message.entities) {
			if (entity.getKi() == null) {
				entity.setKI(new ClientEntityKI(entity));
			}
			this.entityPool.addEntity(entity);
		}
	}
}
