package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.low.GotoKI;

public class SillyWalkerKI extends BaseKI {

	public SillyWalkerKI(Entity entity) {
		super(entity);
	}

	@Override
	protected boolean condition() {
		return this.entity.positionAndDimensionModuleCache.isAvailable();
	}

	@Override
	protected KIState initImpl() {
		return null;
	}

	@Override
	protected KIState updateImpl() {
		this.changeActiveKI(new GotoKI(this.entity, (int) (Math.random() * 100), (int) (Math.random() * 100)), new KICallback());
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {

	}

}
