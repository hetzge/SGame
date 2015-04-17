package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.BaseKICallback;
import de.hetzge.sgame.entity.ki.low.FindFixEntityKI;
import de.hetzge.sgame.entity.ki.low.GotoEntityKI;
import de.hetzge.sgame.entity.ki.low.WaitKI;

public class SillyGotoKI extends BaseKI {

	@Override
	protected boolean callImpl() {
		final FindFixEntityKI findFixEntityKI = new FindFixEntityKI();

		this.changeActiveKI(findFixEntityKI, new BaseKICallback() {
			@Override
			public void onSuccess() {
				SillyGotoKI.this.changeActiveKI(new GotoEntityKI(findFixEntityKI.getResult()));
			}

			@Override
			public void onFailure() {
				SillyGotoKI.this.changeActiveKI(new WaitKI(5000));
			}
		});
		return true;
	}

}
