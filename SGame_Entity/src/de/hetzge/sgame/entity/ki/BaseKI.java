package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.common.IF_DependencyInjection;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.entity.Entity;

public abstract class BaseKI implements IF_DependencyInjection {

	public static final BaseKICallback EMPTY_KI_CALLBACK = new BaseKICallback() {
	};
	public static final BaseKICallback LOG_KI_CALLBACK = new BaseKICallback() {
		@Override
		public void onFailure() {
			System.out.println("KI -> FAILURE");
		}

		@Override
		public void onSuccess() {
			System.out.println("KI -> SUCCESS");
		}
	};

	protected Entity entity;
	protected BaseKI parent;
	protected BaseKI activeKI;
	protected BaseKICallback activeKICallback;

	public void changeActiveKI(BaseKI activceKI) {
		this.changeActiveKI(activceKI, BaseKI.EMPTY_KI_CALLBACK);
	}

	protected void changeActiveKI(BaseKI activeKI, BaseKICallback callback) {
		Log.KI.info("Change KI for entity " + this.entity + " to " + activeKI);

		this.entity.setText(activeKI.getClass().getName());

		activeKI.parent = this;
		activeKI.entity = this.entity;

		this.activeKI = activeKI;
		this.activeKI.activeKICallback = callback;
	}

	public boolean call() {
		if (this.activeKI != null) {
			BaseKI activeKIBefore = this.activeKI;
			boolean active = this.activeKI.call();
			if (!active) {
				if (this.activeKI == activeKIBefore) {
					this.activeKI = null;
				}
			}
			return true;
		} else {
			return this.callImpl();
		}
	}

	public BaseKI currentActiveKI() {
		if (this.activeKI != null) {
			return this.activeKI.currentActiveKI();
		} else {
			return this;
		}
	}

	/**
	 * Return true if the ki is still active or false if the ki is finish
	 */
	protected abstract boolean callImpl();

}
