package de.hetzge.sgame.entity;

import java.io.Serializable;

import de.hetzge.sgame.common.IF_DependencyInjection;

public class EntityReference implements Serializable, IF_DependencyInjection {

	private final String entityId;
	private transient Entity entity;

	public EntityReference(String entityId) {
		this.entityId = entityId;
	}

	public Entity get() {
		if (this.entity == null) {
			this.entity = this.get(EntityPool.class).getEntityById(this.entityId);
		}
		return this.entity;
	}
}
