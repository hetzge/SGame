package de.hetzge.sgame.network;

import java.util.List;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.MessageConfig;

public class NetworkModule implements IF_Module {

	private class NetworkThread extends Thread {
		@Override
		public void run() {
			while (true) {
				Util.sleep(100);
				List<Object> messages = MessageConfig.INSTANCE.messagePool.flush();
				for (Object message : messages) {
					NetworkConfig.INSTANCE.peer.sendMessage(message);
				}
			}
		}
	}

	private final NetworkThread networkThread;

	public NetworkModule() {
		this.networkThread = new NetworkThread();
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
	public void postInit() {
		NetworkConfig.INSTANCE.peer.connect();
		this.networkThread.start();
	}

	@Override
	public void update() {
	}

}
