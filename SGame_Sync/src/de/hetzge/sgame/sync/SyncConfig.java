package de.hetzge.sgame.sync;


public class SyncConfig {

	public static final SyncConfig INSTANCE = new SyncConfig();

	public final SyncPool syncPool = new SyncPool();

	private SyncConfig() {
	}

}
