package de.hetzge.sgame.entity;

public class EntityConfig {

	public static final EntityConfig INSTANCE = new EntityConfig();

	public final EntityPool entityPool = new EntityPool();
	public final EntityFactory entityFactory = new EntityFactory();

	private EntityConfig() {
	}

}
