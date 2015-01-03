package de.hetzge.sgame.entity.message;

import java.io.Serializable;
import java.util.List;

import de.hetzge.sgame.entity.Entity;

public class AddEntitiesMessage implements Serializable {

	public final List<Entity> entities;

	public AddEntitiesMessage(List<Entity> entities) {
		this.entities = entities;
	}
}
