package de.hetzge.sgame.common;

public final class UUID {
	private UUID() {
	}

	public static String generateKey() {
		return java.util.UUID.randomUUID().toString();
	}
}
