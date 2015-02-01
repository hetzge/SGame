package de.hetzge.sgame.entity.ki.low;

import java.util.Set;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;

public class FindFixEntityKI extends BaseKI {

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
		Set<PositionAndDimensionModule> positionAndDimensionModules = this.entityPool.getEntityModulesByModuleClass(PositionAndDimensionModule.class);
		for (PositionAndDimensionModule positionAndDimensionModule : positionAndDimensionModules) {
			if (positionAndDimensionModule.isFixed() && Math.random() > 0.5d) {
				this.result = positionAndDimensionModule.getEntity();
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
