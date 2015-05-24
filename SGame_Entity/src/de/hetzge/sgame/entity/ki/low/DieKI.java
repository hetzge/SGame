package de.hetzge.sgame.entity.ki.low;

import de.hetzge.sgame.entity.api.IF_EntityApi;
import de.hetzge.sgame.entity.ki.BaseKI;

public class DieKI extends BaseKI {

	private final IF_EntityApi entityApi;

	public DieKI(IF_EntityApi entityApi) {
		this.entityApi = entityApi;
	}

	@Override
	protected boolean callImpl() {
		this.entityApi.removeEntity(this.entity);
		return false;
	}

}
