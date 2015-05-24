package de.hetzge.sgame.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.common.exception.SomeException;
import de.hetzge.sgame.message.BaseMessage;
import de.hetzge.sgame.message.BatchMessage;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.network.connection.FSTConnection;

public class Server implements Peer {

	private final Map<String, FSTConnection> clients = new HashMap<>();

	private final FSTConfiguration fstConfiguration;
	private final NetworkConfig networkConfig;
	private final MessageConfig messageConfig;
	private final MessageHandlerPool messageHandlerPool;

	public Server(FSTConfiguration fstConfiguration, NetworkConfig networkConfig, MessageConfig messageConfig, MessageHandlerPool messageHandlerPool) {
		this.fstConfiguration = fstConfiguration;
		this.networkConfig = networkConfig;
		this.messageConfig = messageConfig;
		this.messageHandlerPool = messageHandlerPool;
	}

	@Override
	public void connect() {
		Thread acceptClientsThread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(this.networkConfig.networkData.port)) {
				while (true) {
					Log.NETWORK.info("Server wait for connection");

					Socket socket = serverSocket.accept();
					this.networkConfig.serverLifecycle.onClientConnected();
					FSTConnection connection = new FSTConnection(socket, this.fstConfiguration);

					Log.NETWORK.info("Server received connection");

					Object registerMessage = connection.read();

					Log.NETWORK.info("Server received register message from client");

					this.networkConfig.serverLifecycle.onClientRegister(registerMessage);

					BatchMessage initClientBatchMessage = this.collectInitClientMessage();
					connection.write(initClientBatchMessage);

					Log.NETWORK.info("Server answered register message to client");

					new AcceptMessageThread(connection, this.messageHandlerPool).start();

					synchronized (this.clients) {
						this.clients.put(UUID.generateKey(), connection);
					}

					Log.NETWORK.info("Server started accept message thread");
				}
			} catch (Exception e) {
				throw new SomeException(e);
			}
		}, "accept_clients_thread");
		acceptClientsThread.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
		acceptClientsThread.start();
	}

	@Override
	public void sendMessage(BaseMessage message) {
		synchronized (this.clients) {
			for (Map.Entry<String, FSTConnection> entry : this.clients.entrySet()) {
				try {
					message.sendTimestamp = System.currentTimeMillis();
					entry.getValue().write(message);
				} catch (Exception e) {
					throw new NetworkException(e);
				}
			}
		}
	}

	private BatchMessage collectInitClientMessage() {
		BatchMessage batchMessage = new BatchMessage();
		for (Object object : this.messageConfig.serverToNewClientMessages) {
			if (object instanceof Supplier) {
				Supplier<? extends BaseMessage> callback = (Supplier<? extends BaseMessage>) object;
				batchMessage.add(callback.get());
			} else {
				batchMessage.add((BaseMessage) object);
			}
		}
		return batchMessage;
	}
}
