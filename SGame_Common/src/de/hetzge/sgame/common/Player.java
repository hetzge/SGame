package de.hetzge.sgame.common;

public final class Player {

	public static final int ALL_ID = -1;
	public static final int GAIA_ID = 0;

	public static int nextPlayerId = 1;

	private Player() {
	}

	public synchronized static int nextPlayerId() {
		return Player.nextPlayerId++;
	}

}
