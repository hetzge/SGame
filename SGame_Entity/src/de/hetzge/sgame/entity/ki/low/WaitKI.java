package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.common.timer.Timer;
import de.hetzge.sgame.entity.ki.BaseKI;

public class WaitKI extends BaseKI {

	private final Timer timer;

	public WaitKI(long timespanInMs) {
		this.timer = new Timer(timespanInMs);
		this.timer.isTime();
	}

	@Override
	protected boolean callImpl() {
		if (this.timer.isTime()) {
			this.activeKICallback.onSuccess();
			return false;
		} else {
			return true;
		}
	}

}
