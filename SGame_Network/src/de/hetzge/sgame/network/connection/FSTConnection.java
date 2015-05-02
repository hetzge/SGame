package de.hetzge.sgame.network.connection;

import java.io.IOException;
import java.net.Socket;

import org.nustaq.net.TCPObjectSocket;
import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.message.BaseMessage;

public class FSTConnection implements IF_Connection {

	private final TCPObjectSocket tcpObjectSocket;
	private final FSTConfiguration fstConfiguration;

	public FSTConnection(Socket socket, FSTConfiguration fstConfiguration) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(socket, fstConfiguration);
		this.fstConfiguration = fstConfiguration;
	}

	@Override
	public BaseMessage read() throws Exception {
		return (BaseMessage) this.tcpObjectSocket.readObject();
	}

	@Override
	public void write(BaseMessage message) throws Exception {
		// SizeOf sizeOf = SizeOf.newInstance();
		// System.out.println("Write: " + sizeOf.deepSizeOf(Integer.MAX_VALUE,
		// true, message).getCalculated());

		System.out.println("write: " + message.getClass().getName());
		this.tcpObjectSocket.writeObject(message);
		this.tcpObjectSocket.flush();
	}

}
