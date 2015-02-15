package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.ki.low.GotoKI;

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
		this.changeActiveKI(new GotoKI(this.entity, new XY(500, 500)));
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
