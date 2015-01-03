package de.hetzge.sgame.network;

import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.message.MessageConfig;

public class AcceptMessageThread extends Thread {

	public AcceptMessageThread(Connection connection) {
		super(() -> {
			while (true) {
				try {
					Object message = connection.read();
					MessageConfig.INSTANCE.messageHandlerPool.handleMessage(message);
				} catch (Exception e) {
					throw new NetworkException(e);
				}
			}
		});
		this.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
	}

}
