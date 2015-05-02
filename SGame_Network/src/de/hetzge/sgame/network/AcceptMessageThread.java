package de.hetzge.sgame.network;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.exception.DefaultUncaughtExceptionHandler;
import de.hetzge.sgame.common.exception.NetworkException;
import de.hetzge.sgame.common.timer.Timer;
import de.hetzge.sgame.message.BaseMessage;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.network.connection.IF_Connection;

public class AcceptMessageThread extends Thread {

	private final IF_Connection connection;
	private final MessageHandlerPool messageHandlerPool;
	private final Timer pingTimer = new Timer(3000);

	public AcceptMessageThread(IF_Connection connection, MessageHandlerPool messageHandlerPool) {
		super("accept_message_thread");
		this.connection = connection;
		this.messageHandlerPool = messageHandlerPool;
		this.setUncaughtExceptionHandler(DefaultUncaughtExceptionHandler.INSTANCE);
	}

	@Override
	public void run() {
		while (true) {
			BaseMessage message = null;
			try {
				message = this.connection.read();
			} catch (Exception ex) {
				throw new NetworkException(ex);
			}

			if (this.pingTimer.isTime() && message.sendTimestamp != null) {
				System.out.println("Ping: " + (System.currentTimeMillis() - message.sendTimestamp));
			}

			try {
				this.messageHandlerPool.handleMessage(message);
			} catch (Exception ex) {
				throw new IllegalStateException("Error while handling message", ex);
			}

			System.out.println("Accept");
			Util.sleep(10);
		}
	}

}