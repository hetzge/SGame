package de.hetzge.sgame.message;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BatchMessage extends BaseMessage {

	private final List<BaseMessage> messages = new LinkedList<>();

	public BatchMessage() {
	}

	public void add(BaseMessage message) {
		this.messages.add(message);
	}

	public void addAll(BaseMessage... messages) {
		for (BaseMessage message : messages) {
			this.add(message);
		}
	}

	public void addCollection(Collection<? extends BaseMessage> messages) {
		for (BaseMessage message : messages) {
			this.add(message);
		}
	}

	public List<BaseMessage> getMessages() {
		return this.messages;
	}

	public int size() {
		return this.messages.size();
	}

}
