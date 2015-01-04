package de.hetzge.sgame.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.common.exception.SomeException;
import de.hetzge.sgame.message.BatchMessage;
import de.hetzge.sgame.message.MessageConfig;

public class Server implements Peer {

	private final Map<String, Connection> clients = new HashMap<>();

	public Server() {
	}

	@Override
	public void connect() {
		Thread acceptClientsThread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(NetworkConfig.INSTANCE.networkData.port)) {
				while (true) {

					Log.NETWORK.info("Server wait for connection");

					Socket socket = serverSocket.accept();
					NetworkConfig.INSTANCE.serverLifecycle.onClientConnected();
					Connection connection = new Connection(socket);

					Log.NETWORK.info("Server received connection");

					Object registerMessage = connection.read();

					Log.NETWORK.info("Server received register message from client");

					NetworkConfig.INSTANCE.serverLifecycle.onClientRegister(registerMessage);

					connection.write(this.collectInitClientMessage());

					Log.NETWORK.info("Server answered register message to client");

					new AcceptMessageThread(connection).start();

					synchronized (this.clients) {
						this.clients.put(UUID.generateKey(), connection);
					}

					Log.NETWORK.info("Server started accept message thread");
				}
			} catch (Exception e) {
				throw new SomeException(e);
			}
		});
		acceptClientsThread.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
		acceptClientsThread.start();
	}

	@Override
	public void sendMessage(Object message) {
		synchronized (this.clients) {
			for (Map.Entry<String, Connection> entry : this.clients.entrySet()) {
				try {
					entry.getValue().write(message);
				} catch (Exception e) {
					throw new NetworkException(e);
				}
			}
		}
	}

	private BatchMessage collectInitClientMessage() {
		BatchMessage batchMessage = new BatchMessage();
		for (Object object : MessageConfig.INSTANCE.serverToNewClientMessages) {
			if (object instanceof IF_Callback) {
				IF_Callback<Object> callback = (IF_Callback<Object>) object;
				batchMessage.add(callback.callback());
			} else {
				batchMessage.add(object);
			}
		}
		return batchMessage;
	}
}