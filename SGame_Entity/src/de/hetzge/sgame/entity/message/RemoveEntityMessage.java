package de.hetzge.sgame.entity.message;

import java.io.Serializable;

import de.hetzge.sgame.entity.Entity;

public class RemoveEntityMessage implements Serializable {

	public final Entity entity;

	public RemoveEntityMessage(Entity entity) {
		this.entity = entity;
	}

}
