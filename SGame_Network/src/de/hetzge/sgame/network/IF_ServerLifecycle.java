package de.hetzge.sgame.network;

public interface IF_ServerLifecycle {

	public void onClientConnected();

	public void onClientRegister(Object message);

}
