package de.hetzge.sgame.network;

import de.hetzge.sgame.message.BaseMessage;

public interface Peer {

	public void sendMessage(BaseMessage message);

	public void connect();

}
