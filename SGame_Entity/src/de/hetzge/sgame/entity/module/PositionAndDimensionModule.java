package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.IF_SetupPositionInterpolate;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.geometry.InterpolateRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.ActiveEntityMap;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class PositionAndDimensionModule extends BaseEntityModule implements IF_SetupPositionInterpolate {

	private final SyncProperty<InterpolateRectangle> dimensionSyncProperty = this.createSyncProperty(new InterpolateRectangle());
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
		this.set(this.pathPosition.getCurrentWaypoint(), this.calculateDurationForDistance(this.pathPosition.getDistanceToWaypointBefore()));
	}

	public void unsetPath() {
		this.pathSyncProperty.setValue(null);
		this.pathPosition = null;
		this.stopMoving();
	}

	public void stopMoving() {
		Position currentPosition = new Position(this.getPositionAndDimensionRectangle().getPosition());
		this.set(currentPosition, currentPosition, 0);
	}

	public void continueOnPath() {
		if (this.pathPosition != null) {
			if (this.pathPosition.continueOnPath(this.getPositionAndDimensionRectangle().getPosition())) {
				if (this.entity.renderableModuleCache.isAvailable()) {
					this.entity.renderableModuleCache.get().setOrientation(this.pathPosition.getOrientationFromWaypointBeforeToNext());
				}
				this.set(this.pathPosition.getCurrentWaypoint(), this.calculateDurationForDistance(this.pathPosition.getDistanceToWaypointBefore()));
			}
		}
	}

	private long calculateDurationForDistance(float distance) {
		return (long) (distance / this.speedPerMsSyncProperty.getValue());
	}

	public boolean reachedEndOfPath() {
		if (this.pathPosition != null) {
			return this.pathPosition.reachedEndOfPath(this.getPositionAndDimensionRectangle().getPosition());
		}
		return this.getPositionAndDimensionRectangle().getEndPosition().distance(this.getPositionAndDimensionRectangle().getPosition()) < 1f;
	}

	public void setPosition(InterpolatePosition position) {
		this.dimensionSyncProperty.getValue().setPosition(position);
		this.dimensionSyncProperty.setChanged();
	}

	public void setDimension(Dimension dimension) {
		this.dimensionSyncProperty.getValue().setDimension(dimension);
		this.dimensionSyncProperty.setChanged();
	}

	public IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> getPositionAndDimensionRectangle() {
		return this.dimensionSyncProperty.getValue().immutable();
	}

	@Override
	public void set(Position startValue, Position endValue, long timeSpanInMs) {
		this.dimensionSyncProperty.getValue().set(startValue, endValue, timeSpanInMs);
		this.dimensionSyncProperty.setChanged();
	}

	@Override
	public void set(Position endValue, long timeSpanInMs) {
		this.dimensionSyncProperty.getValue().set(endValue, timeSpanInMs);
		this.dimensionSyncProperty.setChanged();
	}

	@Override
	public void set(Position startValue, Position endValue, long startTimeInMs, long endTimeInMs) {
		this.dimensionSyncProperty.getValue().set(startValue, endValue, startTimeInMs, endTimeInMs);
		this.dimensionSyncProperty.setChanged();
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

}
