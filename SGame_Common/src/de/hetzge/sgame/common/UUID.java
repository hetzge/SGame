package de.hetzge.sgame.common;

public final class UUID {

	private static int lastId = 0;

	private UUID() {
	}

	public static String generateKey() {
		return java.util.UUID.randomUUID().toString();
	}

	public static synchronized int generateId() {
		int id = UUID.lastId + 1;
		UUID.lastId = id;
		return id;
	}
}
