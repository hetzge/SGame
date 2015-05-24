package de.hetzge.sgame.network;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import de.hetzge.sgame.message.BaseMessage;
import de.hetzge.sgame.message.BatchMessage;
import de.hetzge.sgame.message.MessageHandlerPool;

public class NetworkConfig {

	public PeerRole peerRole;
	public Peer peer;
	public NetworkData networkData = new NetworkData("127.0.0.1", 4321);
	public BaseMessage registerMessage = new BatchMessage();
	public final List<Supplier<? extends BaseMessage>> initClientMessageCallbacks = new LinkedList<>();

	private final MessageHandlerPool messageHandlerPool;

	public NetworkConfig(MessageHandlerPool messageHandlerPool) {
		this.messageHandlerPool = messageHandlerPool;
	}

	public IF_ServerLifecycle serverLifecycle = new IF_ServerLifecycle() {

		@Override
		public void onClientRegister(Object message) {
		}

		@Override
		public void onClientConnected() {
		}
	};

	public IF_ClientLifecycle clientLifecycle = new IF_ClientLifecycle() {
		@Override
		public void onClientRegisterSuccess(Object registerAnswer) {
			NetworkConfig.this.messageHandlerPool.handleMessage(registerAnswer);
		}
	};

}
