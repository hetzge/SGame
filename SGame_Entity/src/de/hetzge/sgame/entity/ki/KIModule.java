package de.hetzge.sgame.entity.ki;

import java.util.Collection;

import de.hetzge.sgame.common.PathfinderThread;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;

public class KIModule implements IF_Module {

	private final KIConfig kIConfig;
	private final EntityPool entityPool;
	private final PathfinderThread pathfinderThread;

	public KIModule(KIConfig kIConfig, EntityPool entityPool, PathfinderThread pathfinderThread) {
		this.kIConfig = kIConfig;
		this.entityPool = entityPool;
		this.pathfinderThread = pathfinderThread;
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
		this.pathfinderThread.start();
	}

	@Override
	public void update() {
		Collection<Entity> entities = this.entityPool.getEntities();
		for (Entity entity : entities) {
			entity.updateKI();
		}
	}

}
