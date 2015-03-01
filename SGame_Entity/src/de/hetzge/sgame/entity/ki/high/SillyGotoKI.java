package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.KICallback;
import de.hetzge.sgame.entity.ki.low.FindFixEntityKI;
import de.hetzge.sgame.entity.ki.low.GotoEntityKI;
import de.hetzge.sgame.entity.ki.low.WaitKI;

public class SillyGotoKI extends BaseKI {

	private final EntityOnMapService onMapService = this.get(EntityOnMapService.class);

	public SillyGotoKI(Entity entity) {
		super(entity);
	}

	@Override
	protected boolean condition() {
		return true;
	}

	@Override
	protected KIState updateImpl() {
		FindFixEntityKI findFixEntityKI = new FindFixEntityKI(this.entity);

		this.changeActiveKI(findFixEntityKI, new KICallback() {
			{
				this.on(KIState.FAILURE, () -> SillyGotoKI.this.changeActiveKI(new WaitKI(SillyGotoKI.this.entity, 5000)));
				this.on(KIState.SUCCESS, () -> SillyGotoKI.this.changeActiveKI(new GotoEntityKI(SillyGotoKI.this.entity, (findFixEntityKI.getResult()))));
			}
		});
		return KIState.ACTIVE;
	}

	@Override
	protected KIState initImpl() {
		return KIState.ACTIVE;
	}

	@Override
	protected void finishImpl() {
	}

}
