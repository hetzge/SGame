package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.IF_SetupPositionInterpolate;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.geometry.InterpolateRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class PositionAndDimensionModule extends BaseEntityModule implements IF_SetupPositionInterpolate {

	private final SyncProperty<InterpolateRectangle> dimensionSyncProperty = new SyncProperty<InterpolateRectangle>(new InterpolateRectangle());
	private final SyncProperty<Float> speedPerMsSyncProperty = new SyncProperty<Float>(0.02f);
	private boolean fixed = false;

	private Path path;
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
		this.path = path;
		this.pathPosition = new PathPosition(path, 0);
		this.set(this.pathPosition.getCurrentWaypoint(), 5000);
	}

	public void unsetPath() {
		this.path = null;
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
				this.set(this.pathPosition.getCurrentWaypoint(), (long) (this.pathPosition.getDistanceToWaypointBefore() / this.speedPerMsSyncProperty.getValue()));
			}
		}
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

	public boolean hasPath() {
		return this.path != null && this.pathPosition != null;
	}

}
