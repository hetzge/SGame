package de.hetzge.sgame.entity.ki.high;

import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.entity.ki.low.GotoKI;

public class SillyWalkerKI extends BaseKI {
	@Override
	protected boolean callImpl() {
		if (!this.entity.isFixedPosition()) {
			return false;
		}
		this.changeActiveKI(new GotoKI((int) (Math.random() * 100 + 10), (int) (Math.random() * 100 + 10)));
		return true;
	}
}
