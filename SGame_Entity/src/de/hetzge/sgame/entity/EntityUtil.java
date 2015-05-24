package de.hetzge.sgame.entity;

import de.hetzge.sgame.common.newgeometry2.IF_Dimension_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.XY;

public final class EntityUtil {

	private EntityUtil() {
	}

	/**
	 * Checks if a entity is near enought to a other entity to interact with it.
	 */
	public static boolean isNearEnought(Entity entity, Entity goalEntity) {
		IF_Position_Immutable centeredEntityPosition = entity.getRenderRectangle().getCenter();
		IF_Position_Immutable centeredGoalEntityPosition = goalEntity.getRenderRectangle().getCenter();

		XY absoluteDif = centeredEntityPosition.dif(centeredGoalEntityPosition).abs();
		IF_Dimension_Immutable entityDimension = entity.getRenderRectangle().getDimension();
		IF_Dimension_Immutable otherEntityHalfDimension = goalEntity.getRenderRectangle().getHalfDimension();

		boolean isNearHorizontal = absoluteDif.getFX() < entityDimension.getWidth() + otherEntityHalfDimension.getWidth();
		boolean isNearVertical = absoluteDif.getFY() < entityDimension.getHeight() + otherEntityHalfDimension.getHeight();

		return isNearHorizontal && isNearVertical;
	}

}
