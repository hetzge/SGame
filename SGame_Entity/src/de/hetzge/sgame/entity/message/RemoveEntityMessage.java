package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.message.BaseMessage;

public class RemoveEntityMessage extends BaseMessage {

	public final Entity entity;

	public RemoveEntityMessage(Entity entity) {
		this.entity = entity;
	}

}
