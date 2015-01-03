package de.hetzge.sgame.message;

import java.util.LinkedList;
import java.util.List;

public class MessagePool {

	private final List<Object> buffer = new LinkedList<>();

	public synchronized List<Object> flush() {
		LinkedList<Object> result = new LinkedList<>(this.buffer);
		this.buffer.clear();
		return result;
	}

	public synchronized void addMessage(Object message) {
		if (MessageConfig.INSTANCE.enableMessagePool) {
			this.buffer.add(message);
		}
	}

}
