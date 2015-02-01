package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.entity.ki.BaseKI.KIState;

public class KICallback {
	Runnable[] runnables = new Runnable[KIState.values().length];

	public KICallback() {
	}

	public KICallback on(KIState state, Runnable runnable) {
		this.runnables[state.ordinal()] = runnable;
		return this;
	}

	public void call(KIState state) {
		if (state == null) {
			return;
		}
		Runnable runnable = this.runnables[state.ordinal()];
		if (runnable != null) {
			runnable.run();
		}
	}
}
