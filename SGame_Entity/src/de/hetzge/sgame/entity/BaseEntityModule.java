package de.hetzge.sgame.entity;

import java.io.Serializable;

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



	public Entity getEntity() {
		return this.entity;
	}

	public abstract void updateImpl();

	public abstract void initImpl();

}
