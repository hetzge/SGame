package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastTable;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Predicator;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.common.definition.IF_Lifecycle;
import de.hetzge.sgame.entity.api.IF_EntityApi;

public class EntityPool implements IF_Lifecycle {

	private final FastCollection<Entity> entities = new FastTable<Entity>().shared();
	private final Map<String, Entity> entitiesById = new FastMap<String, Entity>().shared();
	private final Map<IF_EntityType, Set<Entity>> entitiesByType = new FastMap<IF_EntityType, Set<Entity>>().shared();

	/**
	 * If the added entity should be synced see
	 * {@link IF_EntityApi#addEntity(Entity)}
	 */
	public void addEntity(Entity entity) {
		entity.init();

		// add
		this.entities.add(entity);

		// add by id
		this.entitiesById.put(entity.getId(), entity);

		// add by type
		Set<Entity> set = this.entitiesByType.get(entity.getType());
		if (set == null) {
			set = new HashSet<>();
			this.entitiesByType.put(entity.getType(), set);
		}
		set.add(entity);
	}

	/**
	 * If the removed entity should be synced see
	 * {@link IF_EntityApi#removeEntity(Entity)}
	 */
	public void removeEntity(Entity entity) {
		entity.remove();

		// remove
		boolean removed = this.entities.remove(entity);

		// remove by id
		boolean removedById = this.entitiesById.remove(entity.getId()) != null;

		// remove from by type
		Set<Entity> set = this.entitiesByType.get(entity.getType());
		boolean removedByType = false;
		if (set != null) {
			removedByType = set.remove(entity);
		}

		Log.LOG.info("removed entity " + entity.getId() + " from entity pool " + removed + " " + removedById + " " + removedByType);
	}

	public Entity getEntityById(String id) {
		Entity entity = this.entitiesById.get(id);
		if (entity == null) {
			throw new IllegalStateException("Entity with id " + id + " didn't exist.");
		}
		return entity;
	}

	public Set<Entity> getEntitiesByType(IF_EntityType entityType) {
		Set<Entity> set = this.entitiesByType.get(entityType);
		if (set == null) {
			return Collections.emptySet();
		} else {
			return set;
		}
	}

	public Collection<Entity> getEntities() {
		return new ArrayList<Entity>(this.entities);
	}

	public Collection<Entity> getEntities(Predicator<Entity> predicator) {
		return predicator.filterByMatchingAll(this.entities);
	}

	@Override
	public void init() {
		// entities are init when added
	}

	@Override
	public void update() {
		for (Entity entity : this.entities) {
			if (entity == null) {
				continue;
			}
			entity.update();
		}
	}

	@Override
	public void remove() {
	}

}
