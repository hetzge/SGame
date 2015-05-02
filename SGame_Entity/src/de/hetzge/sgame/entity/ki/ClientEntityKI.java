package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.common.service.MoveOnMapService;
import de.hetzge.sgame.entity.Entity;

public class ClientEntityKI extends BaseKI {

	private final MoveOnMapService moveOnMapService;

	public ClientEntityKI(Entity entity) {
		this.entity = entity;
		this.moveOnMapService = this.get(MoveOnMapService.class);
	}

	@Override
	protected boolean callImpl() {
		this.moveOnMapService.move(this.entity);
		return true;
	}

}
