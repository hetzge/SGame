package de.hetzge.sgame.sync;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.sync.message.SyncMessage;

public class SyncProperty<TYPE extends Serializable> implements Serializable {

	private String key;
	private TYPE value;
	private TYPE oldValue;

	public SyncProperty() {
		this(null);
	}

	public SyncProperty(TYPE value) {
		this(value, UUID.generateKey());
	}

	public SyncProperty(TYPE value, String key) {
		this(value, null, key);
	}

	public SyncProperty(TYPE value, TYPE oldValue, String key) {
		this.value = value;
		this.key = key;
		this.setOldValue(oldValue);

		Application.INJECTOR.resolve(Dependency.dependency(SyncPool.class)).registerSyncProperty(this);
	}

	private void setOldValue(TYPE oldValue) {
		this.oldValue = SerializationUtils.clone(oldValue);
	}

	public void setValue(TYPE value) {
		this.value = value;
		this.onSetValue(this);
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
		if (this.isChanged()) {
			this.setOldValue(this.value);
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
		TYPE valueBefore = SerializationUtils.clone(this.value);
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

	public void onSetValue(SyncProperty<TYPE> syncProperty) {
		// to override
	}

}
