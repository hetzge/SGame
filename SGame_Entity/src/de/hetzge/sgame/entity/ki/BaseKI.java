package de.hetzge.sgame.entity.ki;

import java.util.EnumSet;

import de.hetzge.sgame.entity.Entity;

public abstract class BaseKI {

	protected enum KIState {
		SUCCESS, FAILURE, ACTIVE, INIT_FAILURE;
		public static final EnumSet<KIState> FINISH_STATES = EnumSet.of(SUCCESS, FAILURE);
	}

	protected class KICallback {
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

	protected final Entity entity;

	private KICallback activeCallback;
	private BaseKI activeKI;

	public BaseKI(Entity entity) {
		this.entity = entity;
	}

	protected void changeActiveKI(BaseKI activceKI, KICallback callback) {
		if (activceKI.condition()) {
			this.activeKI = activceKI;
			this.activeCallback = callback;
			KIState state = this.activeKI.init();
			this.callCallback(state);
		} else {
			this.callCallback(KIState.INIT_FAILURE);
		}
	}

	public KIState update() {
		if (this.activeKI != null) {
			KIState state = this.activeKI.update();
			this.callCallback(state);
			if (KIState.FINISH_STATES.contains(state)) {
				this.activeKI.finish();
				this.activeKI = null;
				this.activeCallback = null;
			}
			return state;
		} else {
			return this.updateImpl();
		}
	}

	private KIState init() {
		return this.initImpl();
	}

	public void finish() {

	}

	private void callCallback(KIState state) {
		if (this.activeCallback != null) {
			this.activeCallback.call(state);
		}
	}

	protected abstract boolean condition();

	protected abstract KIState updateImpl();

	protected abstract KIState initImpl();

	protected abstract void finishImpl();

}
