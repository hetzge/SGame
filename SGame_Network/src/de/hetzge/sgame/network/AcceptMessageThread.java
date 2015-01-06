package de.hetzge.sgame.network;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.message.MessageConfig;

public class AcceptMessageThread extends Thread {

	private final Connection connection;

	public AcceptMessageThread(Connection connection) {
		super("accept_message_thread");
		this.connection = connection;
		this.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object message = this.connection.read();
				MessageConfig.INSTANCE.messageHandlerPool.handleMessage(message);
			} catch (Exception e) {
				throw new NetworkException(e);
			}
			Util.sleep(10);
		}
	}

}
