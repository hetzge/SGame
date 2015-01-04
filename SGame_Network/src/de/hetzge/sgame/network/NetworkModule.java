package de.hetzge.sgame.network;

import java.util.List;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.MessageConfig;

public class NetworkModule implements IF_Module {

	private boolean connected = false;

	public NetworkModule() {
	}

	@Override
	public void init() {
		MessageConfig.INSTANCE.enableMessagePool = true;

		switch (NetworkConfig.INSTANCE.peerRole) {
		case CLIENT:
			NetworkConfig.INSTANCE.peer = new Client();
			break;
		case SERVER:
			NetworkConfig.INSTANCE.peer = new Server();
			break;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void update() {

		// TODO refactor extra Lifecycle (preupdate oder sowas)
		if (this.connected == false) {
			NetworkConfig.INSTANCE.peer.connect();
			this.connected = true;
		}

		List<Object> messages = MessageConfig.INSTANCE.messagePool.flush();
		for (Object message : messages) {
			NetworkConfig.INSTANCE.peer.sendMessage(message);
		}
	}

}