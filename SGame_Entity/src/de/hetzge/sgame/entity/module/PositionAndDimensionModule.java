package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableRectangle;
import de.hetzge.sgame.common.geometry.IF_SetupPositionInterpolate;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.geometry.InterpolateRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class PositionAndDimensionModule extends BaseEntityModule implements IF_SetupPositionInterpolate {

	private final SyncProperty<InterpolateRectangle> dimensionSyncProperty = new SyncProperty<InterpolateRectangle>(new InterpolateRectangle());

	/*
	 * for reuse to avoid positon object creation
	 */
	private final Position movePosition = new Position();

	public PositionAndDimensionModule(Entity entity) {
		super(entity);
	}

	public void setPosition(InterpolatePosition position) {
		this.dimensionSyncProperty.getValue().setPosition(position);
		this.dimensionSyncProperty.setChanged();
	}

	public void setDimension(Dimension dimension) {
		this.dimensionSyncProperty.getValue().setDimension(dimension);
		this.dimensionSyncProperty.setChanged();
	}

	public IF_ImmutableRectangle<InterpolatePosition, Dimension> getPositionAndDimensionRectangle() {
		return this.dimensionSyncProperty.getValue().immutable();
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
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
}
