package de.hetzge.sgame.common.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import de.hetzge.sgame.common.definition.IF_Callback;

public final class Serializer {

	private final FSTConfiguration fstConfiguration;

	public Serializer(FSTConfiguration fstConfiguration) {
		this.fstConfiguration = fstConfiguration;
	}

	@Deprecated
	public byte[] serialize(Object object) throws IOException {

		// TODO callbacks tun die noch ?
		if (object instanceof IF_Callback) {
			IF_Callback<?> callback = (IF_Callback<?>) object;
			object = callback.callback();
		}

		FSTObjectOutput objectOutput = this.fstConfiguration.getObjectOutput();
		objectOutput.writeObject(object);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(objectOutput.getBuffer(), 0, objectOutput.getWritten());
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return byteArray;
	}

	@Deprecated
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		FSTObjectInput objectInput = this.fstConfiguration.getObjectInput(byteArrayInputStream);
		Object readObject = objectInput.readObject();
		byteArrayInputStream.close();
		return readObject;
	}

	public byte[] toByteArray(Object object) {
		return this.fstConfiguration.asByteArray(object);
	}

}
