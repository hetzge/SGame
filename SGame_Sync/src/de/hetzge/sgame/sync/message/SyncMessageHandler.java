package de.hetzge.sgame.sync.message;

import de.hetzge.sgame.message.IF_MessageHandler;
import de.hetzge.sgame.sync.SyncPool;
import de.hetzge.sgame.sync.SyncProperty;

public class SyncMessageHandler implements IF_MessageHandler<SyncMessage> {

	private final SyncPool syncPool;

	public SyncMessageHandler(de.hetzge.sgame.sync.SyncPool syncPool) {
		this.syncPool = syncPool;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handle(SyncMessage message) {
		SyncProperty syncProperty = this.syncPool.getPropertyByKey(message.key);
		if (syncProperty != null) {
			syncProperty.setValueResetChange(message.value);
		}
	}
}
