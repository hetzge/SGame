package de.hetzge.sgame.entity;

import java.io.Serializable;

public abstract class BaseEntityModule implements Serializable {

	protected final Entity entity;

	public BaseEntityModule(Entity entity) {
		this.entity = entity;
	}

	public abstract void init();

	public abstract void update();

}
