package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.KICallback;
import de.hetzge.sgame.entity.ki.low.FindFixEntityKI;
import de.hetzge.sgame.entity.ki.low.GotoKI;
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
				this.on(KIState.FAILURE, () -> SillyGotoKI.this.changeActiveKI(new WaitKI(SillyGotoKI.this.entity, 5000), new KICallback()));
				this.on(KIState.SUCCESS, () -> {
					IF_Position_ImmutableView goalPosition = SillyGotoKI.this.onMapService.findPositionAround(findFixEntityKI.getResult(), SillyGotoKI.this.entity);
					if (goalPosition != null) {
						SillyGotoKI.this.changeActiveKI(new GotoKI(SillyGotoKI.this.entity, goalPosition));
					} else {
						SillyGotoKI.this.changeActiveKI(new WaitKI(SillyGotoKI.this.entity, 1000l));
					}
				});
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
