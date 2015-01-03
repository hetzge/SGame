package de.hetzge.sgame.message;

import java.util.LinkedList;
import java.util.List;

public class MessageConfig {

	public static final MessageConfig INSTANCE = new MessageConfig();

	public boolean enableMessagePool = false;

	public final MessagePool messagePool = new MessagePool();
	public final MessageHandlerPool messageHandlerPool = new MessageHandlerPool();

	public final List<Object> serverToNewClientMessages = new LinkedList<>();

	private MessageConfig() {
	}

}
