package de.hetzge.sgame.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.entity.module.RenderableModule;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.RenderUtil;

public class EntityPool implements IF_Renderable<IF_RenderableContext> {

	// TODO use concurrent javolution collections

	private final FastCollection<Entity> entities = new FastTable<Entity>().parallel();
	private final FastMap<String, Entity> entitiesById = new FastMap<String, Entity>().parallel();
	private final FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>> entitiesByModule = new FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>>().parallel();
	private final FastMap<IF_EntityType, Set<Entity>> entitiesByType = new FastMap<IF_EntityType, Set<Entity>>().parallel();

	public void addEntity(Entity entity) {
		entity.init();

		// add
		this.entities.add(entity);

		// add by id
		this.entitiesById.put(entity.getId(), entity);

		// add by modules
		List<Class<? extends BaseEntityModule>> allRegisteredModules = entity.getAllRegisteredModules();
		for (Class<? extends BaseEntityModule> moduleClazz : allRegisteredModules) {
			FastSet<Entity> set = this.entitiesByModule.get(moduleClazz);
			if (set == null) {
				set = new FastSet<>();
				this.entitiesByModule.put(moduleClazz, set);
			}
			set.add(entity);
		}

		// add by type
		Set<Entity> set = this.entitiesByType.get(entity.getType());
		if (set == null) {
			set = new HashSet<>();
			this.entitiesByType.put(entity.getType(), set);
		}
		set.add(entity);
	}

	public void removeEntity(Entity entity) {
		// remove
		this.entities.remove(entity);

		// remove by id
		this.entitiesById.remove(entity.getId());

		// remove from by modules
		List<Class<? extends BaseEntityModule>> allRegisteredModules = entity.getAllRegisteredModules();
		for (Class<? extends BaseEntityModule> moduleClazz : allRegisteredModules) {
			Set<Entity> set = this.entitiesByModule.get(moduleClazz);
			if (set != null) {
				set.remove(entity);
			}
		}

		// remove from by type
		Set<Entity> set = this.entitiesByType.get(entity.getType());
		if (set != null) {
			set.remove(entity);
		}
	}

	public Entity getEntityById(String id) {
		Entity entity = this.entitiesById.get(id);
		if (entity == null)
			throw new IllegalStateException("Entity with id " + id + " didn't exist.");
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

	public Set<Entity> getEntitiesByModule(Class<? extends BaseEntityModule> entityModuleClazz) {
		Set<Entity> set = this.entitiesByModule.get(entityModuleClazz);
		if (set == null) {
			return Collections.emptySet();
		} else {
			return set;
		}
	}

	public List<Entity> getEntitiesCopy() {
		return new LinkedList<>(this.entities);
	}

	public void init() {
		for (Entity entity : this.entities) {
			entity.init();
		}
	}

	public void update() {
		for (Entity entity : this.entities) {
			entity.update();
		}
	}

	@Override
	public void render(IF_RenderableContext context) {
		Set<Entity> entitiesByModule = EntityConfig.INSTANCE.entityPool.getEntitiesByModule(RenderableModule.class);
		for (Entity entity : entitiesByModule) {
			RenderableModule renderModule = entity.getModule(RenderableModule.class);
			RenderUtil.render(context, renderModule);
		}
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

}