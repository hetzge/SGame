package de.hetzge.sgame.entity;

import de.hetzge.sgame.common.Context;

public final class EntityContext extends Context<EntityModule> {

	public static final EntityContext INSTANCE = new EntityContext();

	private EntityContext() {
	}

}
