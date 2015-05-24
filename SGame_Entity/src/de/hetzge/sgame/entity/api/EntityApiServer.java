package de.hetzge.sgame.entity.api;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.message.NewEntityMessage;
import de.hetzge.sgame.entity.message.RemoveEntityMessage;
import de.hetzge.sgame.message.MessagePool;

public class EntityApiServer extends EntityApi {

	private final MessagePool messagePool;

	public EntityApiServer(EntityPool entityPool, MessagePool messagePool) {
		super(entityPool);
		this.messagePool = messagePool;
	}

	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);

		// sync
		NewEntityMessage newEntityMessage = new NewEntityMessage(entity);
		this.messagePool.addMessage(newEntityMessage);
	}

	@Override
	public void removeEntity(Entity entity) {
		super.removeEntity(entity);

		// sync
		RemoveEntityMessage removeEntityMessage = new RemoveEntityMessage(entity);
		this.messagePool.addMessage(removeEntityMessage);
	}

}
