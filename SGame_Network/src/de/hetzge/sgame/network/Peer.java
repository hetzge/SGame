package de.hetzge.sgame.network;

public interface Peer {

	public void sendMessage(Object message);

	public void connect();

}
