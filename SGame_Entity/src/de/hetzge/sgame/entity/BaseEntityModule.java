package de.hetzge.sgame.entity;

import java.io.Serializable;

import de.hetzge.sgame.entity.module.CollisionModule;
import de.hetzge.sgame.entity.module.PathfinderModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.entity.module.RenderableModule;

public abstract class BaseEntityModule implements Serializable {

	/*
	 * TODO refactor einheitliches caching
	 */

	/*
	 * cache enity modules
	 */

	protected class EntityModuleCache<ENTITY_MODULE extends BaseEntityModule> {
		private ENTITY_MODULE module;
		private boolean available = false;

		public EntityModuleCache(Class<ENTITY_MODULE> entityModuleClazz) {
			if (BaseEntityModule.this.entity.hasModule(entityModuleClazz)) {
				this.available = true;
				this.module = BaseEntityModule.this.entity.getModule(entityModuleClazz);
			}
		}

		public ENTITY_MODULE get() {
			return this.module;
		}

		public boolean isAvailable() {
			return this.available;
		}
	}

	protected transient EntityModuleCache<CollisionModule> collisionModuleCache;
	protected transient EntityModuleCache<PathfinderModule> pathfinderModuleCache;
	protected transient EntityModuleCache<PositionAndDimensionModule> positionAndDimensionModuleCache;
	protected transient EntityModuleCache<RenderableModule> renderableModuleCache;

	protected final Entity entity;

	public BaseEntityModule(Entity entity) {
		this.entity = entity;
	}

	public final void init() {
		this.collisionModuleCache = new EntityModuleCache<>(CollisionModule.class);
		this.pathfinderModuleCache = new EntityModuleCache<>(PathfinderModule.class);
		this.positionAndDimensionModuleCache = new EntityModuleCache<>(PositionAndDimensionModule.class);
		this.renderableModuleCache = new EntityModuleCache<>(RenderableModule.class);
		this.initImpl();
	}

	public abstract void initImpl();

	public final void update() {
		this.updateImpl();
	}

	public abstract void updateImpl();

}
