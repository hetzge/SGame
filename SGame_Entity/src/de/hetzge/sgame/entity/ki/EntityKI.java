package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.high.SillyGotoKI;

// TODO welche Rolle spielt diese Klasse ? EntityKI zu allgemein ...
public class EntityKI extends BaseKI {

	public EntityKI(Entity entity) {
		super(entity);
	}

	@Override
	protected boolean condition() {
		return false;
	}

	@Override
	protected KIState updateImpl() {
		this.changeActiveKI(new SillyGotoKI(this.entity));
		return null;
	}

	@Override
	protected KIState initImpl() {
		return null;
	}

	@Override
	protected void finishImpl() {
	}

}
