package de.hetzge.sgame.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.entity.module.RenderableModule;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.Viewport;

public class EntityPool implements IF_Renderable<IF_RenderableContext> {

	private final FastCollection<Entity> entities = new FastTable<Entity>().parallel();
	private final FastMap<String, Entity> entitiesById = new FastMap<String, Entity>().parallel();
	private final FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>> entitiesByModule = new FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>>().parallel();
	private final FastMap<IF_EntityType, Set<Entity>> entitiesByType = new FastMap<IF_EntityType, Set<Entity>>().parallel();
	private final FastMap<Class<? extends BaseEntityModule>, FastSet<BaseEntityModule>> entityModulesByModuleClass = new FastMap<Class<? extends BaseEntityModule>, FastSet<BaseEntityModule>>().parallel();

	private final ActiveEntityMap activeEntityMap;
	private final Viewport viewport;
	private final RenderService renderService;

	public EntityPool(ActiveEntityMap activeEntityMap, Viewport viewport, RenderService renderService) {
		this.activeEntityMap = activeEntityMap;
		this.viewport = viewport;
		this.renderService = renderService;
	}

	public void addEntity(Entity entity) {
		entity.init();

		// add
		this.entities.add(entity);

		// add by id
		this.entitiesById.put(entity.getId(), entity);

		// add by modules
		Collection<Class<? extends BaseEntityModule>> allModuleClasses = entity.getAllModuleClasses();
		for (Class<? extends BaseEntityModule> moduleClazz : allModuleClasses) {
			FastSet<Entity> set = this.entitiesByModule.get(moduleClazz);
			if (set == null) {
				set = new FastSet<>();
				this.entitiesByModule.put(moduleClazz, set);
			}
			set.add(entity);
		}

		// add modules
		Collection<BaseEntityModule> allModules = entity.getAllModules();
		for (BaseEntityModule baseEntityModule : allModules) {
			FastSet<BaseEntityModule> set = this.entityModulesByModuleClass.get(baseEntityModule.getClass());
			if (set == null) {
				set = new FastSet<>();
				this.entityModulesByModuleClass.put(baseEntityModule.getClass(), set);
			}
			set.add(baseEntityModule);
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
		Collection<Class<? extends BaseEntityModule>> allModuleClasses = entity.getAllModuleClasses();
		for (Class<? extends BaseEntityModule> moduleClazz : allModuleClasses) {
			Set<Entity> set = this.entitiesByModule.get(moduleClazz);
			if (set != null) {
				set.remove(entity);
			}
		}

		// remove modules
		Collection<BaseEntityModule> allModules = entity.getAllModules();
		for (BaseEntityModule baseEntityModule : allModules) {
			FastSet<BaseEntityModule> set = this.entityModulesByModuleClass.get(baseEntityModule.getClass());
			if (set != null) {
				set.remove(baseEntityModule);
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

	public Set<Entity> getEntitiesByModule(Class<? extends BaseEntityModule> entityModuleClazz) {
		Set<Entity> set = this.entitiesByModule.get(entityModuleClazz);
		if (set == null) {
			return Collections.emptySet();
		} else {
			return set;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntityModule> Set<T> getEntityModulesByModuleClass(Class<T> entityModuleClazz) {
		FastSet<BaseEntityModule> set = this.entityModulesByModuleClass.get(entityModuleClazz);
		if (set == null) {
			return Collections.emptySet();
		} else {
			return (Set<T>) set;
		}
	}

	public Collection<Entity> getEntities() {
		return this.entities.unmodifiable();
	}

	public void init() {
		// entities are init when added
	}

	public void update() {
		for (Entity entity : this.entities) {
			entity.update();
		}
	}

	@Override
	public void render(IF_RenderableContext context) {

		this.viewport.iterateVisibleTiles((int x, int y) -> {
			Collection<Entity> entities = this.activeEntityMap.getConnectedObjects(x, y);
			for (Entity entity : entities) {
				if (entity.renderableModuleCache.isAvailable()) {
					RenderableModule renderableModule = entity.renderableModuleCache.get();
					this.renderService.render(context, renderableModule.getRenderedRectangle(), renderableModule.getRenderableKey());
				}
			}

			// TODO return null weg machen
				return null;
			});
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {

		this.viewport.iterateVisibleTiles((int x, int y) -> {
			Collection<Entity> entities = this.activeEntityMap.getConnectedObjects(x, y);
			for (Entity entity : entities) {
				if (entity.renderableModuleCache.isAvailable()) {
					RenderableModule renderableModule = entity.renderableModuleCache.get();

					this.renderService.render(context, renderableModule.getRenderedRectangle(), PredefinedRenderId.RECTANGLE);
				}
			}

			// TODO return null weg machen
				return null;
			});

	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

}
