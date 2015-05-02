package de.hetzge.sgame.entity.message;

import java.util.Collection;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.message.BaseMessage;

public class AddEntitiesMessage extends BaseMessage {

	public final Collection<Entity> entities;

	public AddEntitiesMessage(Collection<Entity> entities) {
		this.entities = entities;
	}
}
