package de.hetzge.sgame.entity.module;

import java.util.LinkedList;

import de.hetzge.sgame.common.AStarUtil;
import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;

public class PathfinderModule extends BaseEntityModule {

	public PathfinderModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
	}

	@Override
	public void updateImpl() {
	}

	public void goToGoal(int goalX, int goalY, IF_Collision collision) {
		if (!this.entity.hasModule(PositionAndDimensionModule.class)) {
			return;
		}

		boolean[][] entityCollision = new boolean[0][];
		if (this.entity.hasModule(CollisionModule.class)) {
			CollisionModule collisionModule = this.entity.getModule(CollisionModule.class);
			entityCollision = collisionModule.getCollisionTileArray();
		}

		PositionAndDimensionModule positionAndDimensionModule = this.entity.getModule(PositionAndDimensionModule.class);
		IF_ImmutablePosition position = positionAndDimensionModule.getPositionAndDimensionRectangle().getPosition();

		int entityPositionX = CommonConfig.INSTANCE.map.convertPxInCollisionTile(position.getX());
		int entityPositionY = CommonConfig.INSTANCE.map.convertPxInCollisionTile(position.getY());

		int mapCollisionWidth = collision.getWidthInTiles();
		int mapCollisionHeight = collision.getHeightInTiles();

		int[][] rating = new int[mapCollisionWidth][];
		for (int x = 0; x < mapCollisionWidth; x++) {
			rating[x] = new int[mapCollisionHeight];
		}

		LinkedList<Position> path = AStarUtil.findPath(rating, entityPositionX, entityPositionY, goalX, goalY, entityCollision);

	}

}
