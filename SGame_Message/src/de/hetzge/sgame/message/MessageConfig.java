package de.hetzge.sgame.message;

import java.util.LinkedList;
import java.util.List;

public class MessageConfig {

	public boolean enableMessagePool = false;
	public final List<Object> serverToNewClientMessages = new LinkedList<>();

	public MessageConfig() {
	}

}
