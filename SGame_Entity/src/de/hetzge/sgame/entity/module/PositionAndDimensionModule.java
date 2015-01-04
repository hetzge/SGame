package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.sync.SyncProperty;

public class PositionAndDimensionModule extends BaseEntityModule {

	private final SyncProperty<Rectangle> dimensionSyncProperty = new SyncProperty<Rectangle>(new Rectangle());

	/*
	 * for reuse to avoid positon object creation
	 */
	private final Position movePosition = new Position();

	public PositionAndDimensionModule(Entity entity) {
		super(entity);
	}

	public void setPosition(Position position) {
		this.dimensionSyncProperty.getValue().setPosition(position);
		this.dimensionSyncProperty.setChanged();
	}

	public void setDimension(Dimension dimension) {
		this.dimensionSyncProperty.getValue().setDimension(dimension);
		this.dimensionSyncProperty.setChanged();
	}

	public IF_ImmutableRectangle getPositionAndDimensionRectangle() {
		return this.dimensionSyncProperty.getValue().immutable();
	}

	public void move(Orientation orientation, float speed) {
		synchronized (this.movePosition) {
			this.movePosition.setX(speed);
			this.movePosition.setY(speed);
			Position move = this.movePosition.multiply(orientation.orientationFactorOptimized);
			this.dimensionSyncProperty.getValue().getPosition().mutable().add(move);
			this.dimensionSyncProperty.getValue().recalculateRectangle();
			this.dimensionSyncProperty.setChanged();
		}
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}
}
