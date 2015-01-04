package de.hetzge.sgame.sync;

import java.util.List;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.BatchMessage;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.sync.message.SyncMessage;
import de.hetzge.sgame.sync.message.SyncMessageHandler;
import de.hetzge.sgame.sync.serializer.FSTSyncPropertySerializer;

public class SyncModule implements IF_Module {

	@Override
	public void init() {
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(SyncMessage.class, new SyncMessageHandler());
		CommonConfig.INSTANCE.fst.registerSerializer(SyncProperty.class, new FSTSyncPropertySerializer(), true);
	}

	@Override
	public void update() {
		List<SyncMessage> syncMessages = SyncConfig.INSTANCE.syncPool.collectSyncMessages();
		if (!syncMessages.isEmpty()) {
			BatchMessage batchMessage = new BatchMessage();

			batchMessage.addCollection(syncMessages);
			MessageConfig.INSTANCE.messagePool.addMessage(batchMessage);
		}
	}

}