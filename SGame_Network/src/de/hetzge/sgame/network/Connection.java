package de.hetzge.sgame.network;

import java.io.IOException;
import java.net.Socket;

import org.nustaq.net.TCPObjectSocket;
import org.nustaq.serialization.FSTConfiguration;

public class Connection {

	private final TCPObjectSocket tcpObjectSocket;

	public Connection(Socket socket, FSTConfiguration fstConfiguration) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(socket, fstConfiguration);
	}

	public Object read() throws Exception {
		return this.tcpObjectSocket.readObject();
	}

	public void write(Object message) throws Exception {
		this.tcpObjectSocket.writeObject(message);
		this.tcpObjectSocket.flush();
	}

}
