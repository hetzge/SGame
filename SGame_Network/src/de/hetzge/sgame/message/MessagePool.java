package de.hetzge.sgame.message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessagePool {

	private final List<BaseMessage> buffer = new LinkedList<>();
	private final MessageConfig messageConfig;

	public MessagePool(MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public synchronized List<BaseMessage> flush() {
		List<BaseMessage> result = new ArrayList<>(this.buffer);
		this.buffer.clear();
		return result;
	}

	public synchronized void addMessage(BaseMessage message) {
		if (this.messageConfig.enableMessagePool) {
			this.buffer.add(message);
		}
	}

}
