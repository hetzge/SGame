package de.hetzge.sgame.common.newgeometry2;

public interface IF_Rectangle_Mutable extends IF_Rectangle_Immutable {

	void setCenteredX(float centeredX);

	void setCenteredY(float centeredY);

	void setWidth(float width);

	void setHeight(float height);

	default void setCenteredPosition(IF_Position_Immutable centeredPosition) {
		this.setCenteredPosition(centeredPosition.getFX(), centeredPosition.getFY());
	}

	default void setCenteredPosition(float centeredX, float centeredY) {
		this.setCenteredX(centeredX);
		this.setCenteredY(centeredY);
	}

	default void setDimension(IF_Dimension_Immutable dimension) {
		this.setDimension(dimension.getWidth(), dimension.getHeight());
	}

	default void setDimension(float width, float height) {
		this.setWidth(width);
		this.setHeight(height);
	}

}
