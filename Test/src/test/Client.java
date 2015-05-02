package test;

import java.io.IOException;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptTailer;

public class Client {

	public static void main(String[] args) throws IOException {
		new Client();
	}

	public Client() throws IOException {
		Chronicle chronicle = ChronicleQueueBuilder.remoteTailer().connectAddress("localhost", 1234).build();

		ExcerptTailer reader = chronicle.createTailer();

		while (true) {
			while (!reader.nextIndex()) {
			}
			Object ret = reader.readObject();
			reader.finish();

			System.out.println(ret);
		}

	}

}
