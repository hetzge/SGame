package de.hetzge.sgame.network.connection;

import de.hetzge.sgame.message.BaseMessage;

public interface IF_Connection {

	BaseMessage read() throws Exception;

	void write(BaseMessage message) throws Exception;

}
