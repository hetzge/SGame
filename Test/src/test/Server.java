package test;

import java.io.IOException;
import java.util.Date;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ChronicleQueueBuilder.VanillaChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {
		new Server();
	}


	public Server() throws IOException, InterruptedException {
		VanillaChronicleQueueBuilder vanilla = ChronicleQueueBuilder.vanilla("test").cleanupOnClose(true).cycleLength(3);
		Chronicle chronicle = vanilla.source().bindAddress("localhost", 1234).build();
		ExcerptAppender appender = chronicle.createAppender();

		while(true) {
			appender.startExcerpt();
			appender.writeObject("Server sagt " + new Date());
			appender.finish();
			Thread.sleep(1000);
		}
	}

}
