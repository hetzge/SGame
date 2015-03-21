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

	public FindFixEntityKI(Entity entity) {
		super(entity);

		Log.KI.debug("Created FindFixEntityKI for entity " + entity);
	}

	@Override
	protected boolean condition() {
		return true;
	}

	@Override
	protected KIState updateImpl() {
		List<Entity> foundEntitiesAround = this.onMapService.findEntitiesInAreaAround(this.entity, Entity::isFixedPosition, 100, 1);
		if(!foundEntitiesAround.isEmpty()){
			this.result = foundEntitiesAround.get(0);
			return KIState.SUCCESS;
		}
		return KIState.FAILURE;
	}

	@Override
	protected KIState initImpl() {
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {

	}

	public Entity getResult() {
		return this.result;
	}

}
