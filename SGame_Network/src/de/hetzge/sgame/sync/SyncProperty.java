package de.hetzge.sgame.sync;

import java.io.Serializable;
import java.util.function.Consumer;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.sync.message.SyncMessage;

public class SyncProperty<TYPE> implements Serializable {

	private String key;
	private TYPE value;
	private TYPE oldValue;

	public SyncProperty(TYPE value, String key) {
		this.value = value;
		this.key = key;

		Application.INJECTOR.resolve(Dependency.dependency(SyncPool.class)).registerSyncProperty(this);
	}

	public SyncProperty(TYPE value) {
		this(value, UUID.generateKey());
	}

	public SyncProperty() {
		this(null);
	}

	public void setValue(TYPE value) {
		this.value = value;
		this.onSetValue(this);
	}

	public void setValueResetChange(TYPE value) {
		this.setValue(value);
		this.oldValue = value;
		this.onSetValue(this);
	}

	public void setChanged() {
		this.oldValue = null; // TODO Optimierung damit auch innere Werte auf
		// Änderungen überprüft werden (equals)
	}

	protected SyncMessage flush() {
		if (this.isChanged()) {
			this.oldValue = this.value;
			SyncMessage syncMessage = new SyncMessage();
			syncMessage.key = this.key;
			syncMessage.value = this.value;
			return syncMessage;
		}
		return null;
	}

	public boolean isChanged() {
		return this.isChanged(this.oldValue);
	}

	public boolean isChanged(TYPE otherValue) {
		return (this.value != null && otherValue == null) || (this.value != null && !this.value.equals(otherValue));
	}

	public void change(Consumer<TYPE> consumer) {
		TYPE valueBefore = this.value;
		consumer.accept(this.value);
		if (this.isChanged(valueBefore)) {
			this.setChanged();
		}
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

	public void onSetValue(SyncProperty<TYPE> syncProperty) {
		// to override
	}

}
