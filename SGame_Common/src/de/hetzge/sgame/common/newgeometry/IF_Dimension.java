package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_MutableView;

public interface IF_Dimension extends IF_Dimension_ImmutableView, IF_Dimension_MutableView {

	@Override
	public default void setWidth(float width) {
		this.setX(width);
	}

	@Override
	public default void setHeight(float height) {
		this.setY(height);
	}

	@Override
	public default float getWidth() {
		return Math.abs(this.getX());
	}

	@Override
	public default float getHeight() {
		return Math.abs(this.getY());
	}

}
