package de.hetzge.sgame.entity;

import java.io.Serializable;
import java.util.Collection;

import javolution.util.FastMap;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.definition.IF_EntityType;

public class Entity implements Serializable {

	private final FastMap<Class<? extends BaseEntityModule>, BaseEntityModule> modules = new FastMap<>();
	private final IF_EntityType type;
	private final String id = UUID.generateKey();

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
			if (!this.hasModule(clazz))
				return false;
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

	/**
	 * called when the entity is registered in a pool
	 */
	public void init() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

}
