package de.hetzge.sgame.sync;

import java.util.List;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.message.MessagePool;
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
				List<SyncMessage> syncMessages = SyncModule.this.syncPool.collectSyncMessages();
				for (SyncMessage syncMessage : syncMessages) {
					SyncModule.this.messagePool.addMessage(syncMessage);
				}

				//				if (!syncMessages.isEmpty()) {
				//					BatchMessage batchMessage = new BatchMessage();
				//					batchMessage.addCollection(syncMessages);
				//					SyncModule.this.messagePool.addMessage(batchMessage);
				//				}
				Util.sleep(10);

				// Util.sleep(100);
			}
		}
	}

	private final Thread syncThread;

	private final SyncConfig syncConfig;
	private final SyncPool syncPool;
	private final FSTConfiguration fstConfiguration;
	private final MessageHandlerPool messageHandlerPool;
	private final MessagePool messagePool;

	public SyncModule(SyncConfig syncConfig, SyncPool syncPool, FSTConfiguration fstConfiguration, MessageHandlerPool messageHandlerPool, MessagePool messagePool) {
		this.syncThread = new SyncThread();
		this.syncConfig = syncConfig;
		this.syncPool = syncPool;
		this.fstConfiguration = fstConfiguration;
		this.messageHandlerPool = messageHandlerPool;
		this.messagePool = messagePool;
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(SyncMessage.class, this.get(SyncMessageHandler.class));
		this.fstConfiguration.registerSerializer(SyncProperty.class, this.get(FSTSyncPropertySerializer.class), true);
	}

	@Override
	public void postInit() {
		if (this.syncConfig.isSyncThreadEnabled()) {
			this.syncThread.start();
		}
	}

	@Override
	public void update() {

	}

}
