package de.hetzge.sgame.entity.message;

import java.io.Serializable;

import de.hetzge.sgame.entity.Entity;

public class NewEntityMessage implements Serializable {

	public final Entity entity;

	public NewEntityMessage(Entity entity) {
		this.entity = entity;
	}

}
