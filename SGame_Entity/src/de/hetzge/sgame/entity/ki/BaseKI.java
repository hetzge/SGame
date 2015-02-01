package de.hetzge.sgame.entity.ki;

import java.util.EnumSet;

import de.hetzge.sgame.common.IF_DependencyInjection;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.entity.Entity;

public abstract class BaseKI implements IF_DependencyInjection {

	private static final KICallback EMPTY_KI_CALLBACK = new KICallback();

	protected enum KIState {
		SUCCESS, FAILURE, ACTIVE, INIT_FAILURE;
		public static final EnumSet<KIState> FINISH_STATES = EnumSet.of(SUCCESS, FAILURE);
	}

	protected class LogKICallback extends KICallback {
		public LogKICallback() {
			this.on(KIState.FAILURE, () -> System.out.println("FAILURE"));
			this.on(KIState.INIT_FAILURE, () -> System.out.println("INIT_FAILURE"));
			this.on(KIState.SUCCESS, () -> System.out.println("SUCCESS"));
		}
	}

	protected final Entity entity;

	private KICallback activeCallback;
	private BaseKI activeKI;

	public BaseKI(Entity entity) {
		this.entity = entity;
	}

	protected void changeActiveKI(BaseKI activceKI) {
		this.changeActiveKI(activceKI, BaseKI.EMPTY_KI_CALLBACK);
	}

	protected void changeActiveKI(BaseKI activeKI, KICallback callback) {

		Log.KI.debug("Change KI for entity " + this.entity + " to " + activeKI);

		if (activeKI.condition()) {
			this.activeKI = activeKI;
			this.activeCallback = callback;
			KIState state = this.activeKI.init();
			this.callCallback(state);
		} else {
			this.callCallback(KIState.INIT_FAILURE);
		}
	}

	// TODO mal mit einem durchspielen
	public KIState update() {
		if (this.activeKI != null) {
			KIState state = this.activeKI.update();
			BaseKI activeKIBefore = this.activeKI;
			this.callCallback(state);
			if (KIState.FINISH_STATES.contains(state)) {
				this.activeKI.finish();
				if (this.activeKI == activeKIBefore) {
					this.activeKI = null;
					this.activeCallback = null;
				}
				return KIState.ACTIVE;
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
