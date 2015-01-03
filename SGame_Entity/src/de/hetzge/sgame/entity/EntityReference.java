package de.hetzge.sgame.entity;

import java.io.Serializable;

public class EntityReference implements Serializable {

	private final String entityId;
	private transient Entity entity;

	public EntityReference(String entityId) {
		this.entityId = entityId;
	}

	public Entity get() {
		if (this.entity == null) {
			this.entity = EntityConfig.INSTANCE.entityPool.getEntityById(this.entityId);
		}
		return this.entity;
	}
}
