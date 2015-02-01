package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.timer.Timer;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.BaseKI;

public class WaitKI extends BaseKI {

	private final Timer timer;

	public WaitKI(Entity entity, long timespanInMs) {
		super(entity);
		this.timer = new Timer(timespanInMs);
		this.timer.isTime();
	}

	@Override
	protected boolean condition() {
		return true;
	}

	@Override
	protected KIState updateImpl() {
		if (this.timer.isTime()) {
			return KIState.SUCCESS;
		} else {
			return KIState.ACTIVE;
		}
	}

	@Override
	protected KIState initImpl() {
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
	}

}
