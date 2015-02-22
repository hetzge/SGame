package de.hetzge.sgame.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.Viewport;

public class EntityPool implements IF_Renderable<IF_RenderableContext> {

	private final FastCollection<Entity> entities = new FastTable<Entity>().shared();
	private final FastMap<String, Entity> entitiesById = new FastMap<String, Entity>().shared();
	private final FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>> entitiesByModule = new FastMap<Class<? extends BaseEntityModule>, FastSet<Entity>>().shared();
	private final FastMap<IF_EntityType, Set<Entity>> entitiesByType = new FastMap<IF_EntityType, Set<Entity>>().shared();
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
				this.renderService.render(context, entity.getRenderRectangle(), entity.getRenderableKey());
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
				this.renderService.render(context, entity.getRealRectangle(), PredefinedRenderId.RECTANGLE);
				Path path = entity.getPath();
				if (path != null) {
					this.renderService.render(context, entity.getRenderRectangle(), PredefinedRenderId.LINE);
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
