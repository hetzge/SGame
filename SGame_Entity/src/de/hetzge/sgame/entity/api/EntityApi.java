package de.hetzge.sgame.entity.api;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;

public class EntityApi implements IF_EntityApi {

	private final EntityPool entityPool;

	public EntityApi(de.hetzge.sgame.entity.EntityPool entityPool) {
		this.entityPool = entityPool;
	}

	@Override
	public void addEntity(Entity entity) {
		this.entityPool.addEntity(entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		this.entityPool.removeEntity(entity);
	}

}
