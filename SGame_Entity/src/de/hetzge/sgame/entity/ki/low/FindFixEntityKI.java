package de.hetzge.sgame.entity.ki.low;

import java.util.List;

import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.ki.BaseKI;

public class FindFixEntityKI extends BaseKI {

	// TODO funktion in service auslagern

	private final EntityOnMapService onMapService = this.get(EntityOnMapService.class);
	private Entity result;

	public FindFixEntityKI() {
		Log.KI.info("Created FindFixEntityKI for entity " + this.entity);
	}

	@Override
	protected boolean callImpl() {
		List<Entity> foundEntitiesAround = this.onMapService.findEntitiesInAreaAround(this.entity, Entity::isFixedPosition, 100, 1);
		if (!foundEntitiesAround.isEmpty()) {
			this.result = foundEntitiesAround.get(0);
			this.activeKICallback.onSuccess();
			return false;
		} else {
			this.activeKICallback.onFailure();
			return false;
		}
	}

	public Entity getResult() {
		return this.result;
	}
}
