package de.hetzge.sgame.entity.message;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.message.BaseMessage;

public class NewEntityMessage extends BaseMessage {

	public final Entity entity;

	public NewEntityMessage(Entity entity) {
		this.entity = entity;
	}

}
