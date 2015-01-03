package de.hetzge.sgame.network;

import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.message.MessageConfig;

public class NetworkConfig {

	public static final NetworkConfig INSTANCE = new NetworkConfig();

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
			MessageConfig.INSTANCE.messageHandlerPool.handleMessage(registerAnswer);
		}
	};
	public PeerRole peerRole;
	public Peer peer;
	public NetworkData networkData = new NetworkData("127.0.0.1", 4321);
	public Object registerMessage = new Object();

	public final List<IF_Callback> initClientMessageCallbacks = new LinkedList<>();

	private NetworkConfig() {
	}
}
