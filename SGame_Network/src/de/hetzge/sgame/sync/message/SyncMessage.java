package de.hetzge.sgame.sync.message;

import java.io.Serializable;

import de.hetzge.sgame.message.BaseMessage;

public class SyncMessage extends BaseMessage {

	public String key;
	public Serializable value;

}
