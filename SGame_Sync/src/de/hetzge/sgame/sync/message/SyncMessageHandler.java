package de.hetzge.sgame.sync.message;

import de.hetzge.sgame.message.IF_MessageHandler;
import de.hetzge.sgame.sync.SyncConfig;
import de.hetzge.sgame.sync.SyncProperty;

public class SyncMessageHandler implements IF_MessageHandler<SyncMessage> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handle(SyncMessage message) {
		SyncProperty syncProperty = SyncConfig.INSTANCE.syncPool.getPropertyByKey(message.key);
		if (syncProperty != null) {
			syncProperty.setValueResetChange(message.value);
		}
	}
}