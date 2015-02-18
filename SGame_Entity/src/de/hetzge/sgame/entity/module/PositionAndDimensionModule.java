package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.newgeometry.IF_Dimension;
import de.hetzge.sgame.common.newgeometry.IF_Position;
import de.hetzge.sgame.common.newgeometry.InterpolateXY;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.entity.ActiveEntityMap;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class PositionAndDimensionModule extends BaseEntityModule implements IF_Rectangle_ImmutableView {

	private final SyncProperty<InterpolateXY> positionSyncProperty = this.createSyncProperty(new InterpolateXY());
	private final SyncProperty<InterpolateXY> dimensionSyncProperty = this.createSyncProperty(new InterpolateXY());
	private final SyncProperty<Float> speedPerMsSyncProperty = this.createSyncProperty(0.02f);
	private final SyncProperty<Path> pathSyncProperty = this.createSyncProperty(null);

	private final ActiveMap<Entity> entityOnMap = new ActiveEntityMap().setObjectOnPosition(this.entity, 0, 0);
	private ActiveMap<Boolean> activeCollisionMap = new ActiveMap<Boolean>().setObjectInArea(true, 5, 5);
	private boolean fixed = false;
	private PathPosition pathPosition;

	public PositionAndDimensionModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
	}

	@Override
	public void updateImpl() {
	}

	public void setPath(Path path) {
		this.pathSyncProperty.setValue(path);
		this.pathPosition = new PathPosition(path, 0);
		this.positionSyncProperty.change( position -> position.set(this.pathPosition.getCurrentPosition(), this.calculateDurationForDistance(this.pathPosition.getDistanceToWaypointBefore())));
	}

	public void unsetPath() {
		this.pathSyncProperty.setValue(null);
		this.pathPosition = null;
		this.stopMoving();
	}

	public void stopMoving() {
		this.positionSyncProperty.change( position -> position.stop());
	}

	public void continueOnPath() {
		if (this.pathPosition != null) {
			if (this.pathPosition.continueOnPath(this.positionSyncProperty.getValue())) {
				if (this.entity.renderableModuleCache.isAvailable()) {
					this.entity.renderableModuleCache.get().setOrientation(this.pathPosition.getOrientationFromWaypointBeforeToNext());
				}
				this.positionSyncProperty.change( position -> position.set(this.pathPosition.getCurrentPosition(), this.calculateDurationForDistance(this.pathPosition.getDistanceToWaypointBefore())));
			}
		}
	}

	private long calculateDurationForDistance(float distance) {
		return (long) (distance / this.speedPerMsSyncProperty.getValue());
	}

	public boolean reachedEndOfPath() {
		return this.pathPosition == null || this.pathPosition.reachedEndOfPath(this.positionSyncProperty.getValue());
	}

	public void setPosition(IF_Position newPosition) {
		this.positionSyncProperty.change(position -> position.set(newPosition));
	}

	public void setDimension(IF_Dimension newDimension) {
		this.dimensionSyncProperty.change(dimension -> dimension.set(newDimension));
	}

	public boolean isFixed() {
		return this.fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public Path getPath() {
		return this.pathSyncProperty.getValue();
	}

	public boolean hasPath() {
		return this.getPath() != null && this.pathPosition != null;
	}

	public ActiveMap<Entity> getEntityOnMap() {
		return this.entityOnMap;
	}

	/**
	 * set given boolean array as collision tiles
	 */
	public void setCollision(boolean[][] collision) {
		if (collision.length == 0) {
			return;
		}

		int collisionWidthInTiles = collision.length;
		int collisionHeightInTiles = collision[0].length;

		ActiveMap<Boolean> activeMap = new ActiveMap<>();

		for (int x = 0; x < collisionWidthInTiles; x++) {
			for (int y = 0; y < collisionHeightInTiles; y++) {
				activeMap.setObjectOnPosition(collision[x][y], x, y);
			}
		}

		this.activeCollisionMap.unchain();
		this.activeCollisionMap = activeMap;
	}

	public ActiveMap<Boolean> getActiveCollisionMap() {
		return this.activeCollisionMap;
	}

	@Override
	public IF_Position_ImmutableView getCenteredPosition() {
		return this.positionSyncProperty.getValue();
	}

	@Override
	public IF_Dimension_ImmutableView getDimension() {
		return this.dimensionSyncProperty.getValue();
	}


}
