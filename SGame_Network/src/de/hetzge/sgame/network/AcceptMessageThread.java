package de.hetzge.sgame.network;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.message.MessageHandlerPool;

public class AcceptMessageThread extends Thread {

	private final Connection connection;
	private final MessageHandlerPool messageHandlerPool;

	public AcceptMessageThread(Connection connection, MessageHandlerPool messageHandlerPool) {
		super("accept_message_thread");
		this.connection = connection;
		this.messageHandlerPool = messageHandlerPool;
		this.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object message = this.connection.read();
				this.messageHandlerPool.handleMessage(message);
			} catch (Exception e) {
				throw new NetworkException(e);
			}
			Util.sleep(10);
		}
	}

}