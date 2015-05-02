package de.hetzge.sgame.network;

import java.util.List;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.BaseMessage;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessagePool;

public class NetworkModule implements IF_Module {

	private class NetworkThread extends Thread {
		public NetworkThread() {
			super("network_thread");
		}

		@Override
		public void run() {
			while (true) {
				List<BaseMessage> messages = NetworkModule.this.messagePool.flush();
				for (BaseMessage message : messages) {
					NetworkModule.this.networkConfig.peer.sendMessage(message);
				}
				//				Util.sleep(10);
			}
		}
	}

	private final NetworkThread networkThread;
	private final NetworkConfig networkConfig;
	private final MessageConfig messageConfig;
	private final MessagePool messagePool;

	public NetworkModule(NetworkConfig networkConfig, MessageConfig messageConfig, MessagePool messagePool) {
		this.networkThread = new NetworkThread();
		this.networkConfig = networkConfig;
		this.messageConfig = messageConfig;
		this.messagePool = messagePool;
	}

	@Override
	public void init() {
		this.messageConfig.enableMessagePool = true;

		switch (this.networkConfig.peerRole) {
		case CLIENT:
			this.networkConfig.peer = this.get(Client.class);
			break;
		case SERVER:
			this.networkConfig.peer = this.get(Server.class);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void postInit() {
		this.networkConfig.peer.connect();
		this.networkThread.start();
	}

	@Override
	public void update() {
	}

}
