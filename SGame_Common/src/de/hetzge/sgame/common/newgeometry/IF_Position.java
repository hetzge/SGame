package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_MutableView;

public interface IF_Position extends IF_Position_ImmutableView, IF_Position_MutableView {

	@Override
	public default float getFX() {
		return this.getX();
	}

	@Override
	public default float getFY() {
		return this.getY();
	}

	@Override
	public default void setFX(float x) {
		this.setX(x);
	}

	@Override
	public default void setFY(float y) {
		this.setY(y);
	}

}
