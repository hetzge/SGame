package de.hetzge.sgame.entity;

import java.io.Serializable;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.common.newgeometry.IF_Dimension;
import de.hetzge.sgame.common.newgeometry.IF_Position;
import de.hetzge.sgame.common.newgeometry.InterpolateXY;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.entity.ki.EntityKI;
import de.hetzge.sgame.render.DefaultAnimationKey;
import de.hetzge.sgame.render.IF_AnimationKey;
import de.hetzge.sgame.render.RenderableKey;
import de.hetzge.sgame.sync.SyncPool;
import de.hetzge.sgame.sync.SyncProperty;

public class Entity implements Serializable {

	public class RenderRectangle implements IF_Rectangle_ImmutableView {

		/**
		 * The dimension of the entity on the screen.
		 */
		private final SyncProperty<InterpolateXY> dimensionSyncProperty = Entity.this.createSyncProperty(new InterpolateXY());

		@Override
		public IF_Position_ImmutableView getCenteredPosition() {
			return Entity.this.centeredPositionSyncProperty.getValue();
		}

		@Override
		public IF_Dimension_ImmutableView getDimension() {
			return this.dimensionSyncProperty.getValue();
		}
	}

	public class RealRectangle implements IF_Rectangle_ImmutableView {

		/**
		 * The real dimension of the entity in the virtual world.
		 */
		private final SyncProperty<InterpolateXY> realDimensionSyncProperty = Entity.this.createSyncProperty(new InterpolateXY());

		@Override
		public IF_Position_ImmutableView getCenteredPosition() {
			return Entity.this.centeredPositionSyncProperty.getValue();
		}

		@Override
		public IF_Dimension_ImmutableView getDimension() {
			return this.realDimensionSyncProperty.getValue();
		}
	}

	/*
	 * Base entity properties
	 */
	private final String id = UUID.generateKey();
	private transient EntityKI entityKI;
	private final IF_EntityType type;

	/*
	 * Dimension and movement properties
	 */

	/**
	 * The centered position of the entity on the map.
	 */
	private final SyncProperty<InterpolateXY> centeredPositionSyncProperty = this.createSyncProperty(new InterpolateXY());

	private final SyncProperty<Float> speedPerMsSyncProperty = this.createSyncProperty(0.02f);
	private final SyncProperty<Path> pathSyncProperty = this.createSyncProperty(null);

	private final RenderRectangle renderRectangle = new RenderRectangle();
	private final RealRectangle realRectangle = new RealRectangle();
	private final ActiveMap<Entity> entityOnMap = new ActiveEntityMap().setObjectOnPosition(this, 0, 0);
	private ActiveCollisionMap activeCollisionMap = new ActiveCollisionMap(0, 0);
	private boolean fixed = false;
	private PathPosition pathPosition;

	/*
	 * Renderable properties
	 */
	private final SyncProperty<RenderableKey> renderableKeySyncProperty = this.createSyncProperty(new RenderableKey());

	Entity(IF_EntityType type) {
		this.type = type;
	}

	public IF_EntityType getType() {
		return this.type;
	}

	public String getId() {
		return this.id;
	}

	public void setEntityKI(EntityKI entityKI) {
		this.entityKI = entityKI;
	}

	/**
	 * called when the entity is registered in a pool
	 */
	public void init() {
	}

	/**
	 * called in the update cycle
	 */
	public void update() {
	}

	public void updateKI() {
		if (this.entityKI != null) {
			this.entityKI.update();
		}
	}

	private <T> SyncProperty<T> createSyncProperty(T value) {
		return Application.INJECTOR.resolve(Dependency.dependency(SyncPool.class)).createAndRegisterSyncProperty(value);
	}

	/*
	 * Dimension and movement methods
	 */

	public void setPath(Path path) {
		this.pathSyncProperty.setValue(path);
		this.pathPosition = new PathPosition(path, 0);
		this.centeredPositionSyncProperty.change(position -> position.set(this.pathPosition.getCurrentPosition(), this.calculateDurationForDistanceInMilliseconds(this.pathPosition.getDistanceToWaypointBefore())));
		this.setAnimationKey(DefaultAnimationKey.WALK);
	}

	public void unsetPath() {
		this.pathSyncProperty.setValue(null);
		this.pathPosition = null;
		this.stopMoving();
		this.setAnimationKey(DefaultAnimationKey.DEFAULT);
	}

	public boolean isMoving(){
		return !this.fixed && this.centeredPositionSyncProperty.getValue().hasFinished();
	}

	public boolean isNotMovingNotFixedEntity(){
		return !this.isFixedPosition() && !this.isMoving();
	}

	public void stopMoving() {
		this.centeredPositionSyncProperty.change(position -> position.stop());
	}

	public void continueOnPath() {
		if (!this.hasPath()) {
			return;
		}
		if (this.pathPosition.continueOnPath(this.centeredPositionSyncProperty.getValue())) {
			this.setOrientation(this.pathPosition.getOrientationFromWaypointBeforeToNext());
			IF_Position currentWaypoint = this.pathPosition.getCurrentPosition();
			float distanceToCurrentWaypoint = this.pathPosition.getDistanceToWaypointBefore();
			long durationForDistance = this.calculateDurationForDistanceInMilliseconds(distanceToCurrentWaypoint);
			this.centeredPositionSyncProperty.change(position -> position.set(currentWaypoint, durationForDistance));
		}
	}

	private long calculateDurationForDistanceInMilliseconds(float distance) {
		return (long) (distance / this.speedPerMsSyncProperty.getValue());
	}

	public boolean reachedEndOfPath() {
		return this.pathPosition == null || this.pathPosition.reachedEndOfPath(this.centeredPositionSyncProperty.getValue());
	}

	public void setCenteredPosition(IF_Position_ImmutableView newPosition) {
		this.centeredPositionSyncProperty.change(position -> position.set(newPosition));
	}

	public void setPositionA(IF_Position_ImmutableView newPosition) {
		this.setCenteredPosition(newPosition.copy().add(this.renderRectangle.getHalfDimension().asPositionImmutableView()));
	}

	public void setDimension(IF_Dimension newDimension) {
		this.renderRectangle.dimensionSyncProperty.change(dimension -> dimension.set(newDimension));
	}

	public void setRealDimension(IF_Dimension newDimension) {
		this.realRectangle.realDimensionSyncProperty.change(dimension -> dimension.set(newDimension));
	}

	public RenderRectangle getRenderRectangle() {
		return this.renderRectangle;
	}

	public RealRectangle getRealRectangle() {
		return this.realRectangle;
	}

	public boolean isFixedPosition() {
		return this.fixed;
	}

	public void setFixedPosition(boolean fixed) {
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
			throw new IllegalStateException("try to set empty collision");
		}

		int collisionWidthInTiles = collision.length;
		int collisionHeightInTiles = collision[0].length;

		ActiveCollisionMap activeMap = new ActiveCollisionMap(collisionWidthInTiles, collisionHeightInTiles);

		for (int x = 0; x < collisionWidthInTiles; x++) {
			for (int y = 0; y < collisionHeightInTiles; y++) {
				activeMap.setObjectOnPosition(collision[x][y], x, y);
			}
		}

		this.activeCollisionMap.unchain();
		this.activeCollisionMap = activeMap;
	}

	public ActiveCollisionMap getActiveCollisionMap() {
		return this.activeCollisionMap;
	}

	/**
	 * Renderable methods
	 */

	public void setOrientation(Orientation orientation) {
		boolean changed = false;
		if (!this.renderableKeySyncProperty.getValue().orientation.equals(orientation)) {
			changed = true;
		}
		this.renderableKeySyncProperty.getValue().orientation = orientation;
		if (changed) {
			this.renderableKeySyncProperty.setChanged();
		}
	}

	public void setEntityKey(IF_EntityType entityType) {
		boolean changed = false;
		if (!this.renderableKeySyncProperty.getValue().entityType.equals(entityType)) {
			changed = true;
		}
		this.renderableKeySyncProperty.getValue().entityType = entityType;
		if (changed) {
			this.renderableKeySyncProperty.setChanged();
		}
	}

	public void setAnimationKey(IF_AnimationKey animationKey) {
		boolean changed = false;
		if (!this.renderableKeySyncProperty.getValue().animationKey.equals(animationKey)) {
			changed = true;
		}
		this.renderableKeySyncProperty.getValue().animationKey = animationKey;
		if (changed) {
			this.renderableKeySyncProperty.setChanged();
		}
	}

	public RenderableKey getRenderableKey() {
		return this.renderableKeySyncProperty.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Entity other = (Entity) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.id;
	}

}
