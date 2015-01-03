package de.hetzge.sgame.sync;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hetzge.sgame.sync.message.SyncMessage;

public class SyncPool {

	private final Map<String, SyncProperty<?>> syncProperties = new HashMap<>();

	public void registerSyncProperty(SyncProperty<?> syncProperty) {
		this.syncProperties.put(syncProperty.getKey(), syncProperty);
	}

	public SyncProperty<?> getPropertyByKey(String key) {
		return this.syncProperties.get(key);
	}

	public List<SyncMessage> collectSyncMessages() {
		List<SyncMessage> result = new LinkedList<>();
		for (SyncProperty<?> syncProperty : this.syncProperties.values()) {
			SyncMessage flush = syncProperty.flush();
			if (flush != null) {
				result.add(flush);
			}
		}
		return result;
	}

}
