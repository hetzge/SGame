package de.hetzge.sgame.entity;

import java.io.Serializable;
import java.util.Collection;

import javolution.util.FastMap;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.entity.ki.EntityKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.entity.module.RenderableModule;

public class Entity implements Serializable {

	public class EntityModuleCache<ENTITY_MODULE extends BaseEntityModule> {
		private ENTITY_MODULE module;
		private boolean available = false;

		public EntityModuleCache(Class<ENTITY_MODULE> entityModuleClazz) {
			if (Entity.this.hasModule(entityModuleClazz)) {
				this.available = true;
				this.module = Entity.this.getModule(entityModuleClazz);
			}
		}

		public ENTITY_MODULE get() {
			return this.module;
		}

		public boolean isAvailable() {
			return this.available;
		}

		public boolean isNotAvailable() {
			return !this.isAvailable();
		}
	}

	private final FastMap<Class<? extends BaseEntityModule>, BaseEntityModule> modules = new FastMap<>();
	private final String id = UUID.generateKey();
	private transient EntityKI entityKI;
	private final IF_EntityType type;

	Entity(IF_EntityType type) {
		this.type = type;
	}

	public void registerModule(BaseEntityModule entityModule) {
		// dont override existing modules
		if (!this.modules.containsKey(entityModule.getClass())) {
			this.modules.put(entityModule.getClass(), entityModule);
		}
	}

	public void registerModules(BaseEntityModule... entityModules) {
		for (BaseEntityModule entityModule : entityModules) {
			this.registerModule(entityModule);
		}
	}

	public boolean hasModule(Class<? extends BaseEntityModule> moduleClazz) {
		return this.modules.containsKey(moduleClazz);
	}

	public boolean hasModules(Class<? extends BaseEntityModule>... moduleClasses) {
		for (Class<? extends BaseEntityModule> clazz : moduleClasses) {
			if (!this.hasModule(clazz)) {
				return false;
			}
		}
		return true;
	}

	// TODO getModule durch lazy richtige Methoden ersetzen
	@SuppressWarnings("unchecked")
	public <MODULE_TYPE extends BaseEntityModule> MODULE_TYPE getModule(Class<MODULE_TYPE> moduleClazz) {
		return (MODULE_TYPE) this.modules.get(moduleClazz);
	}

	public Collection<Class<? extends BaseEntityModule>> getAllModuleClasses() {
		return this.modules.keySet().unmodifiable();
	}

	public Collection<BaseEntityModule> getAllModules() {
		return this.modules.values().unmodifiable();
	}

	public IF_EntityType getType() {
		return this.type;
	}

	public String getId() {
		return this.id;
	}

	public void setEntityKI(EntityKI entityKI) {
		this.entityKI = entityKI;
	}

	/**
	 * called when the entity is registered in a pool
	 */
	public void init() {
		this.initModuleCaches();
		for (BaseEntityModule baseEntityModule : this.modules.values()) {
			baseEntityModule.init();
		}
	}

	/**
	 * called in the update cycle
	 */
	public void update() {
		for (BaseEntityModule baseEntityModule : this.modules.values()) {
			baseEntityModule.update();
		}
	}

	public void updateKI() {
		if (this.entityKI != null) {
			this.entityKI.update();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Entity other = (Entity) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	// optimized module access

	public transient EntityModuleCache<PositionAndDimensionModule> positionAndDimensionModuleCache;
	public transient EntityModuleCache<RenderableModule> renderableModuleCache;

	private void initModuleCaches() {
		this.positionAndDimensionModuleCache = new EntityModuleCache<>(PositionAndDimensionModule.class);
		this.renderableModuleCache = new EntityModuleCache<>(RenderableModule.class);
	}

	@Override
	public String toString() {
		return this.id;
	}

}
