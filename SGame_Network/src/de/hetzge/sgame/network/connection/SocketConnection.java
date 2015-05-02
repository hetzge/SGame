package de.hetzge.sgame.network.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.hetzge.sgame.message.BaseMessage;

public class SocketConnection implements IF_Connection {

	private final Socket socket;
	private final ObjectInputStream objectInputStream;
	private final ObjectOutputStream objectOutputStream;

	public SocketConnection(Socket socket) throws IOException {
		this.socket = socket;
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
		this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public BaseMessage read() throws Exception {
		return (BaseMessage) this.objectInputStream.readObject();
	}

	@Override
	public void write(BaseMessage message) throws Exception {
		this.objectOutputStream.writeObject(message);
	}

}
