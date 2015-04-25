package de.hetzge.sgame.message;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BatchMessage implements Serializable {

	private final List<Object> messages = new LinkedList<>();

	public void add(Object message) {
		this.messages.add(message);
	}

	public void addAll(Object... messages) {
		for (Object object : messages) {
			this.add(object);
		}
	}

	public void addCollection(Collection<? extends Object> messages) {
		for (Object object : messages) {
			this.add(object);
		}
	}

	public List<Object> getMessages() {
		return this.messages;
	}

	public int size() {
		return this.messages.size();
	}

}
