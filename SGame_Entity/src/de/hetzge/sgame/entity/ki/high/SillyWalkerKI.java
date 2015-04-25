package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.BaseKICallback;
import de.hetzge.sgame.entity.ki.low.GotoKI;
import de.hetzge.sgame.entity.ki.low.WaitKI;

public class SillyWalkerKI extends BaseKI {
	@Override
	protected boolean callImpl() {
		if (this.entity.isFixedPosition()) {
			return false;
		}
		this.changeActiveKI(new GotoKI((int) (Math.random() * 100 + 10), (int) (Math.random() * 100 + 10)), new BaseKICallback() {
			@Override
			public void onFailure() {
				SillyWalkerKI.this.changeActiveKI(new WaitKI(3000));
			}
		});
		return true;
	}
}
