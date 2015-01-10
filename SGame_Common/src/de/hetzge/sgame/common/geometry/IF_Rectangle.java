package de.hetzge.sgame.common.geometry;

public interface IF_Rectangle<POSITION extends IF_Position<?>, DIMENSION extends IF_Dimension<?>> extends IF_ImmutableComplexRectangle<POSITION, DIMENSION> {

	public void setPosition(POSITION position);

	public void setDimension(DIMENSION dimension);

}
