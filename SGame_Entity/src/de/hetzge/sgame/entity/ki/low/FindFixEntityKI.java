package de.hetzge.sgame.entity.ki.low;

import java.util.Collection;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.ki.BaseKI;

public class FindFixEntityKI extends BaseKI {

	// TODO funktion in service auslagern

	private final EntityPool entityPool = this.get(EntityPool.class);
	private Entity result;

	public FindFixEntityKI(Entity entity) {
		super(entity);

		Log.KI.debug("Created FindFixEntityKI for entity " + entity);
	}

	@Override
	protected boolean condition() {
		return true;
	}

	@Override
	protected KIState updateImpl() {
		Collection<Entity> entities = this.entityPool.getEntities();
		for (Entity entity : entities) {
			if (entity.isFixedPosition() && Math.random() > 0.5d) {
				this.result = entity;
				return KIState.SUCCESS;
			}
		}
		return KIState.FAILURE;
	}

	@Override
	protected KIState initImpl() {
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {

	}

	public Entity getResult() {
		return this.result;
	}

}
