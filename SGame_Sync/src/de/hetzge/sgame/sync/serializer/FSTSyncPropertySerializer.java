package de.hetzge.sgame.sync.serializer;

import java.io.IOException;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import de.hetzge.sgame.sync.SyncPool;
import de.hetzge.sgame.sync.SyncProperty;

public class FSTSyncPropertySerializer extends FSTBasicObjectSerializer {

	private final SyncPool syncPool;

	public FSTSyncPropertySerializer(SyncPool syncPool) {
		this.syncPool = syncPool;
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object object, FSTClazzInfo clazzInfo, FSTFieldInfo fieldInfo, int str) throws IOException {
		SyncProperty<Object> syncProperty = (SyncProperty<Object>) object;
		out.writeObject(syncProperty.getValue());
		out.writeObject(syncProperty.getOldValue());
		out.writeStringUTF(syncProperty.getKey());
	}

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTFieldInfo referencedBy) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		SyncProperty<Object> syncProperty = (SyncProperty<Object>) toRead;
		syncProperty.setValue(in.readObject());
		syncProperty.setOldValue(in.readObject());
		syncProperty.setKey(in.readStringUTF());

		this.syncPool.registerSyncProperty(syncProperty);
		super.readObject(in, syncProperty, clzInfo, referencedBy);
	}
}
