package de.hetzge.sgame.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Callback {

	public static interface IF_CallbackOption {
	}

	public class OptionBuilder {
		private final IF_CallbackOption callbackOption;

		public OptionBuilder(IF_CallbackOption callbackOption) {
			this.callbackOption = callbackOption;
		}

		public Callback action(Consumer<Object> consumer) {
			Callback.this.consumersByOption.put(this.callbackOption, consumer);
			return Callback.this;
		}
	}

	private final Map<IF_CallbackOption, Consumer<Object>> consumersByOption = new HashMap<>();

	private Callback() {
	}

	public static Callback create() {
		return new Callback();
	}

	public OptionBuilder option(IF_CallbackOption callbackOption) {
		return new OptionBuilder(callbackOption);
	}

	public void call(IF_CallbackOption callbackOption, Object consume) {
		if (this.consumersByOption.isEmpty()) {
			return;
		}

		Consumer<Object> consumer = this.consumersByOption.get(callbackOption);
		if (consumer != null) {
			consumer.accept(consume);
		}
	}

	public void call(IF_CallbackOption callbackOption) {
		this.call(callbackOption, null);
	}

}
