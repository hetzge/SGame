package de.hetzge.sgame.entity;

import de.hetzge.sgame.common.newgeometry.IF_XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;

public final class EntityUtil {

	private EntityUtil() {
	}

	/**
	 * Checks if a entity is near enought to a other entity to interact with it.
	 */
	public static boolean isNearEnought(Entity entity, Entity goalEntity) {
		IF_Position_ImmutableView centeredEntityPosition = entity.getRenderRectangle().getCenteredPosition();
		IF_Position_ImmutableView centeredGoalEntityPosition = goalEntity.getRenderRectangle().getCenteredPosition();

		IF_XY absoluteDif = centeredEntityPosition.dif(centeredGoalEntityPosition).abs();
		IF_Dimension_ImmutableView entityDimension = entity.getRenderRectangle().getDimension();
		IF_Dimension_ImmutableView otherEntityHalfDimension = goalEntity.getRenderRectangle().getHalfDimension();

		boolean isNearHorizontal = absoluteDif.getX() < entityDimension.getWidth() + otherEntityHalfDimension.getWidth();
		boolean isNearVertical = absoluteDif.getY() < entityDimension.getHeight() + otherEntityHalfDimension.getHeight();

		return isNearHorizontal && isNearVertical;
	}

}
