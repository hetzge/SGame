package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.ki.low.DieKI;
import de.hetzge.sgame.entity.ki.low.WaitKI;

/**
 * This ki is base ki a entity has and which never changes.
 * 
 * In this class could possibility the jobmanagement be done.
 * 
 * @author Markus
 */
public class EntityKI extends BaseKI {

	private EntityOnMapService entityOnMapService;

	public EntityKI(Entity entity) {
		this.entity = entity;
		this.entityOnMapService = this.get(EntityOnMapService.class);
	}

	@Override
	protected boolean callImpl() {
		this.changeActiveKI(new WaitKI(15000), new BaseKICallback() {
			@Override
			public void onSuccess() {
				EntityKI.this.changeActiveKI(EntityKI.this.get(DieKI.class));
			}
		});
		return true;
	}

}
