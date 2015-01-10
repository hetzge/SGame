package de.hetzge.sgame.common.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.definition.IF_Callback;

public final class Serializer {
	private Serializer() {
	}

	@Deprecated
	public static byte[] serialize(Object object) throws IOException {

		// TODO callbacks tun die noch ?
		if (object instanceof IF_Callback) {
			IF_Callback<?> callback = (IF_Callback<?>) object;
			object = callback.callback();
		}

		FSTObjectOutput objectOutput = CommonConfig.INSTANCE.fst.getObjectOutput();
		objectOutput.writeObject(object);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(objectOutput.getBuffer(), 0, objectOutput.getWritten());
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return byteArray;
	}

	@Deprecated
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		FSTObjectInput objectInput = CommonConfig.INSTANCE.fst.getObjectInput(byteArrayInputStream);
		Object readObject = objectInput.readObject();
		byteArrayInputStream.close();
		return readObject;
	}

	public static byte[] toByteArray(Object object) {
		return CommonConfig.INSTANCE.fst.asByteArray(object);
	}

}
