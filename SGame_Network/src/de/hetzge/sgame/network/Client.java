package de.hetzge.sgame.network;

import java.net.InetSocketAddress;
import java.net.Socket;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.exception.NetworkException;

public class Client implements Peer {

	private Connection connection;

	public Client() {
	}

	@Override
	public void connect() {
		try {
			NetworkData networkData = NetworkConfig.INSTANCE.networkData;

			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(networkData.ipAddress, networkData.port), 0);
			Log.NETWORK.info("Client try to connect");

			this.connection = new Connection(socket);

			synchronized (this.connection) {

				Log.NETWORK.info("Client connected");

				this.connection.write(NetworkConfig.INSTANCE.registerMessage);

				Log.NETWORK.info("Client wrote register object");

				Object registerAnswer = this.connection.read();
				NetworkConfig.INSTANCE.clientLifecycle.onClientRegisterSuccess(registerAnswer);

				Log.NETWORK.info("Client handled register answer");

				new AcceptMessageThread(this.connection).start();

				Log.NETWORK.info("Client message receive thread startet");

			}
		} catch (Exception e) {
			throw new NetworkException(e);
		}
	}

	@Override
	public void sendMessage(Object message) {
		synchronized (this.connection) {
			if (this.connection == null)
				throw new IllegalStateException();

			try {
				this.connection.write(message);
			} catch (Exception e) {
				throw new NetworkException(e);
			}
		}
	}
}