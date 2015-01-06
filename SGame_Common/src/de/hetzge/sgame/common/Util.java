package de.hetzge.sgame.common;

import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;

public final class Util {

	private Util() {
	}

	public static float calculateDistance(IF_ImmutablePosition<?> position, IF_ImmutablePosition<?> otherPosition) {
		float a = Math.abs(position.getX() - otherPosition.getX());
		float b = Math.abs(position.getY() - otherPosition.getY());
		return (float) Math.sqrt(a * a + b * b);
	}

	public static void sleep(long timeInMs) {
		try {
			Thread.sleep(timeInMs);
		} catch (InterruptedException e) {
		}
	}

}
