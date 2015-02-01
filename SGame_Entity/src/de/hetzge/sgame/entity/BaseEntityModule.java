package de.hetzge.sgame.entity;

import java.io.Serializable;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.sync.SyncPool;
import de.hetzge.sgame.sync.SyncProperty;

public abstract class BaseEntityModule implements Serializable {

	protected final Entity entity;

	public BaseEntityModule(Entity entity) {
		this.entity = entity;
	}

	public final void init() {
		this.initImpl();
	}

	public final void update() {
		this.updateImpl();
	}

	protected <T> SyncProperty<T> createSyncProperty(T value) {
		return Application.INJECTOR.resolve(Dependency.dependency(SyncPool.class)).createAndRegisterSyncProperty(value);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public abstract void updateImpl();

	public abstract void initImpl();

}
