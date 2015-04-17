package de.hetzge.sgame.message;

import java.util.LinkedList;
import java.util.List;

public class MessagePool {

	private final List<Object> buffer = new LinkedList<>();
	private final MessageConfig messageConfig;

	public MessagePool(MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public synchronized List<Object> flush() {
		LinkedList<Object> result = new LinkedList<>(this.buffer);
		this.buffer.clear();
		return result;
	}

	public synchronized void addMessage(Object message) {
		if (this.messageConfig.enableMessagePool) {
			this.buffer.add(message);
		}
	}

}
