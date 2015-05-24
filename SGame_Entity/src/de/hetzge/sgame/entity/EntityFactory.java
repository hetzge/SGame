package de.hetzge.sgame.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.entity.api.IF_EntityApi;

public class EntityFactory {

	private final Map<IF_EntityType, Consumer<Entity>> factories = new HashMap<>();

	private final IF_EntityApi entityApi;

	public EntityFactory(IF_EntityApi entityApi) {
		this.entityApi = entityApi;
	}

	public void registerFactory(IF_EntityType entityType, Consumer<Entity> consumer) {
		this.factories.put(entityType, consumer);
	}

	@SafeVarargs
	public final void build(IF_EntityType entityType, Consumer<Entity>... moreConsumers) {
		Entity entity = new Entity(entityType);
		Consumer<Entity> consumer = this.factories.get(entityType);
		if (consumer != null) {
			consumer.accept(entity);
		}
		for (Consumer<Entity> moreConsumer : moreConsumers) {
			moreConsumer.accept(entity);
		}

		// add entity to pool
		this.entityApi.addEntity(entity);
	}

}
