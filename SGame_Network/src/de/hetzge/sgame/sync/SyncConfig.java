package de.hetzge.sgame.sync;

public class SyncConfig {

	private boolean syncThreadEnabled = true;

	private SyncConfig() {
	}

	public boolean isSyncThreadEnabled() {
		return this.syncThreadEnabled;
	}

	public void setSyncThreadEnabled(boolean syncThreadEnabled) {
		this.syncThreadEnabled = syncThreadEnabled;
	}

}
