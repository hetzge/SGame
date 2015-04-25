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
			Object message = null;
			try {
				message = this.connection.read();
			} catch (Exception ex) {
				throw new NetworkException(ex);
			}
			try {
				this.messageHandlerPool.handleMessage(message);
			} catch (Exception ex) {
				throw new IllegalStateException("Error while handling message", ex);
			}
			Util.sleep(10);
		}
	}

}