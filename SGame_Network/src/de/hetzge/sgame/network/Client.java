package de.hetzge.sgame.network;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.message.BaseMessage;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.network.connection.FSTConnection;

public class Client implements Peer {

	private FSTConnection connection;

	private final NetworkConfig networkConfig;
	private final FSTConfiguration fstConfiguration;
	private final MessageHandlerPool messageHandlerPool;

	public Client(FSTConfiguration fstConfiguration, NetworkConfig networkConfig, MessageHandlerPool messageHandlerPool) {
		this.networkConfig = networkConfig;
		this.fstConfiguration = fstConfiguration;
		this.messageHandlerPool = messageHandlerPool;
	}

	@Override
	public void connect() {
		try {
			NetworkData networkData = this.networkConfig.networkData;

			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(networkData.ipAddress, networkData.port), 0);
			Log.NETWORK.info("Client try to connect");

			this.connection = new FSTConnection(socket, this.fstConfiguration);

			synchronized (this.connection) {
				Log.NETWORK.info("Client connected");

				this.connection.write(this.networkConfig.registerMessage);

				Log.NETWORK.info("Client wrote register object");

				Object registerAnswer = this.connection.read();
				this.networkConfig.clientLifecycle.onClientRegisterSuccess(registerAnswer);

				Log.NETWORK.info("Client handled register answer");

				new AcceptMessageThread(this.connection, this.messageHandlerPool).start();

				Log.NETWORK.info("Client message receive thread startet");
			}
		} catch (Exception e) {
			throw new NetworkException(e);
		}
	}

	@Override
	public void sendMessage(BaseMessage message) {
		synchronized (this.connection) {
			if (this.connection == null) {
				throw new IllegalStateException();
			}

			try {
				this.connection.write(message);
			} catch (Exception e) {
				throw new NetworkException(e);
			}
		}
	}
}
