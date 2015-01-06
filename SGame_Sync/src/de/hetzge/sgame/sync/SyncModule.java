package de.hetzge.sgame.sync;

import java.util.List;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.BatchMessage;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.sync.message.SyncMessage;
import de.hetzge.sgame.sync.message.SyncMessageHandler;
import de.hetzge.sgame.sync.serializer.FSTSyncPropertySerializer;

public class SyncModule implements IF_Module {

	private class SyncThread extends Thread {

		public SyncThread() {
			super("sync_thread");
		}

		@Override
		public void run() {
			while (true) {
				List<SyncMessage> syncMessages = SyncConfig.INSTANCE.syncPool.collectSyncMessages();
				if (!syncMessages.isEmpty()) {
					BatchMessage batchMessage = new BatchMessage();
					batchMessage.addCollection(syncMessages);
					MessageConfig.INSTANCE.messagePool.addMessage(batchMessage);
				}
				Util.sleep(100);
			}
		}
	}

	private final Thread syncThread;

	public SyncModule() {
		this.syncThread = new SyncThread();
	}

	@Override
	public void init() {
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(SyncMessage.class, new SyncMessageHandler());
		CommonConfig.INSTANCE.fst.registerSerializer(SyncProperty.class, new FSTSyncPropertySerializer(), true);
	}

	@Override
	public void postInit() {
		this.syncThread.start();
	}

	@Override
	public void update() {

	}

}
