package de.hetzge.sgame.message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessageHandlerPool {

	private final Map<Class<?>, List<IF_MessageHandler<?>>> messageHandlers = new HashMap<>();

	public <T> void registerMessageHandler(Class<T> messageClazz, IF_MessageHandler<T> messageHandler) {
		List<IF_MessageHandler<?>> list = this.messageHandlers.get(messageClazz);
		if (list == null) {
			list = new LinkedList<>();
		}
		this.messageHandlers.put(messageClazz, list);
		list.add(messageHandler);
	}

	@SuppressWarnings("unchecked")
	public void handleMessage(Object message) {
		List<IF_MessageHandler<?>> list = this.messageHandlers.get(message.getClass());
		if (list != null) {
			for (IF_MessageHandler messageHandler : list) {
				messageHandler.handle(message);
			}
		}
	}

}
