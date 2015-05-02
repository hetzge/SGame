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
		// write the same value as old value that the sync property can't reach
		// in a changed state.
		out.writeObject(syncProperty.getValue());
		out.writeStringUTF(syncProperty.getKey());
	}

	@Override
	public Object instantiate(Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTFieldInfo referencee, int streamPositioin) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		SyncProperty<Object> syncProperty = new SyncProperty<>();
		syncProperty.setValue(in.readObject());
		syncProperty.setOldValue(in.readObject());
		syncProperty.setKey(in.readStringUTF());

		this.syncPool.registerSyncProperty(syncProperty);

		return syncProperty;
	}
}
