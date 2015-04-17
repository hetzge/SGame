package de.hetzge.sgame.sync;

import java.io.Serializable;
import java.util.function.Consumer;

import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.sync.message.SyncMessage;

public class SyncProperty<TYPE> implements Serializable {

	private String key;
	private TYPE value;
	private TYPE oldValue;

	protected SyncProperty(TYPE value) {
		this.key = UUID.generateKey();
		this.value = value;
	}

	public SyncProperty() {
		this(null);
	}

	public void setValue(TYPE value) {
		this.value = value;
	}

	public void setValueResetChange(TYPE value) {
		this.setValue(value);
		this.oldValue = value;
	}

	public void setChanged() {
		this.oldValue = null; // TODO Optimierung damit auch innere Werte auf
		// Änderungen überprüft werden (equals)
	}

	protected SyncMessage flush() {
		if (this.value != this.oldValue) {
			this.oldValue = this.value;
			SyncMessage syncMessage = new SyncMessage();
			syncMessage.key = this.key;
			syncMessage.value = this.value;
			return syncMessage;
		}
		return null;
	}

	public void change(Consumer<TYPE> consumer){
		consumer.accept(this.value);
		this.setChanged();
	}

	public TYPE getValue() {
		return this.value;
	}

	public TYPE getOldValue() {
		return this.oldValue;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setOldValue(TYPE oldValue) {
		this.oldValue = oldValue;
	}

}
