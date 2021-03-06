package de.hetzge.sgame.common.exception;

public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	public static final DefaultUncaughtExceptionHandler INSTANCE = new DefaultUncaughtExceptionHandler();

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();
	}
}
