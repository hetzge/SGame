package de.hetzge.sgame.message;

public interface IF_MessageHandler<MESSAGE_TYPE> {
	public void handle(MESSAGE_TYPE message);
}
