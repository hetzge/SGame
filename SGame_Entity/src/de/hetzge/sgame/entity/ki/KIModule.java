package de.hetzge.sgame.entity.ki;

import java.util.Collection;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityConfig;

public class KIModule implements IF_Module {

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public void update() {
		Collection<Entity> entities = EntityConfig.INSTANCE.entityPool.getEntities();
		for (Entity entity : entities) {
			entity.updateKI();
		}
	}

}
