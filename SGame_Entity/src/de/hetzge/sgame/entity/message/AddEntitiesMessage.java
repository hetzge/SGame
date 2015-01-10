package de.hetzge.sgame.entity.message;

import java.io.Serializable;
import java.util.Collection;

import de.hetzge.sgame.entity.Entity;

public class AddEntitiesMessage implements Serializable {

	public final Collection<Entity> entities;

	public AddEntitiesMessage(Collection<Entity> entities) {
		this.entities = entities;
	}
}
