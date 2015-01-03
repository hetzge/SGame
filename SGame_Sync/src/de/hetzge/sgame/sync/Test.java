package de.hetzge.sgame.sync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import de.hetzge.sgame.sync.serializer.FSTSyncPropertySerializer;

public class Test {

	public static class XYZ implements Serializable {

		private SyncProperty x;

		public XYZ() {
			super();
			this.x = new SyncProperty(10);

			System.out.println("Call");
		}

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		FSTConfiguration fst = FSTConfiguration.createDefaultConfiguration();
		fst.registerSerializer(SyncProperty.class, new FSTSyncPropertySerializer(), true);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		FSTObjectOutput out = fst.getObjectOutput(byteArrayOutputStream);
		out.writeObject(new XYZ());
		out.close(); // required !

		byte[] byteArray = byteArrayOutputStream.toByteArray();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		FSTObjectInput in = fst.getObjectInput(byteArrayInputStream);
		XYZ result = (XYZ) in.readObject();
		in.close(); // required !

	}

}
