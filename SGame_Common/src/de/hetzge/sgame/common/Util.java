package de.hetzge.sgame.common;

public final class Util {

	private Util() {
	}

	public static void sleep(long timeInMs) {
		try {
			Thread.sleep(timeInMs);
		} catch (InterruptedException e) {
		}
	}

}