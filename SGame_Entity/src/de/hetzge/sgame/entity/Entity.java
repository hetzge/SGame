package de.hetzge.sgame.entity;

import java.io.Serializable;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.common.PathPosition;
import de.hetzge.sgame.common.Player;
import de.hetzge.sgame.common.UUID;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.common.definition.IF_MapMoveable;
import de.hetzge.sgame.common.newgeometry.IF_Dimension;
import de.hetzge.sgame.common.newgeometry.InterpolateXY;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.service.MoveOnMapService;
import de.hetzge.sgame.entity.item.Container;
import de.hetzge.sgame.entity.ki.BaseKI;
import de.hetzge.sgame.render.IF_AnimationKey;
import de.hetzge.sgame.render.RenderableKey;
import de.hetzge.sgame.sync.SyncProperty;

public class Entity implements Serializable, IF_MapMoveable {

	public class RenderRectangle implements IF_Rectangle_ImmutableView {

		/**
		 * The dimension of the entity on the screen.
		 */
		private final SyncProperty<InterpolateXY> dimensionSyncProperty = new SyncProperty<>(new InterpolateXY(), "dimensionSyncProperty" + UUID.generateId());

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
		private final SyncProperty<InterpolateXY> realDimensionSyncProperty = new SyncProperty<>(new InterpolateXY(), "realDimensionSyncProperty" + UUID.generateId());

		@Override
		public IF_Position_ImmutableView getCenteredPosition() {
			return Entity.this.centeredPositionSyncProperty.getValue();
		}

		@Override
		public IF_Dimension_ImmutableView getDimension() {
			return this.realDimensionSyncProperty.getValue();
		}
	}

	public class PathPositionSyncProperty extends SyncProperty<PathPosition> {

		public PathPositionSyncProperty(PathPosition value, String key) {
			super(value, key);
		}

		@Override
		public void onSetValue(de.hetzge.sgame.sync.SyncProperty<PathPosition> syncProperty) {
			PathPosition pathPosition = syncProperty.getValue();
			if (pathPosition != null) {
				pathPosition.setOnPathPositionChangedCallback(x -> {
					Entity.this.syncCenteredPosition();
				});
			}
		};
	}

	/**
	 * The centered position of the entity on the map.
	 */
	private final SyncProperty<IF_Position_MutableView> centeredPositionSyncProperty = new SyncProperty<>(new XY(0f), "centeredPositionSyncProperty" + UUID.generateId());
	private final SyncProperty<Float> speedPerTickSyncProperty = new SyncProperty<>(1f, "speedPerTickSyncProperty");
	private final SyncProperty<PathPosition> pathPositionSyncProperty = new PathPositionSyncProperty(null, "pathPositionSyncProperty" + UUID.generateId());
	private final SyncProperty<RenderableKey> renderableKeySyncProperty = new SyncProperty<>(new RenderableKey(), "renderableKeySyncProperty" + UUID.generateId());
	private final SyncProperty<String> textSyncProperty = new SyncProperty<>("", "textSyncProperty" + UUID.generateId());

	private final RenderRectangle renderRectangle = new RenderRectangle();
	private final RealRectangle realRectangle = new RealRectangle();
	private final ActiveMap<Entity> entityOnMap = new ActiveEntityMap().setObjectOnPosition(this, 0, 0);
	private ActiveCollisionMap activeCollisionMap = new ActiveCollisionMap(0, 0);
	private transient boolean fixed = false;
	private transient int playerId = Player.GAIA_ID;
	private transient BaseKI ki;
	private final String id = UUID.generateId() + "";
	private final IF_EntityType type;

	/**
	 * This container describes the the needs of the entity.
	 */
	private final Container containerNeeds = new Container();

	/**
	 * This container describes the items that can taken by other entities.
	 */
	private final Container containerProvides = new Container();

	/**
	 * This container describes the items the entity is carrying with him. As
	 * example to transport to a other entity.
	 */
	private final Container containerHas = new Container();

	Entity(IF_EntityType type) {
		this.type = type;
	}

	public IF_EntityType getType() {
		return this.type;
	}

	public String getId() {
		return this.id;
	}

	public void setKI(BaseKI ki) {
		this.ki = ki;
	}

	public BaseKI getKi() {
		return this.ki;
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
		if (this.ki != null) {
			this.ki.call();
		}
	}

	/*
	 * Dimension and movement methods
	 */

	/**
	 * Don't call this directly. Use
	 * {@link MoveOnMapService#setPath(IF_MapMoveable, Path)} instead.
	 */
	@Override
	public void setPath(Path path) {
		PathPosition pathPosition = new PathPosition(path, 0);
		this.pathPositionSyncProperty.setValue(pathPosition);
	}

	public void syncCenteredPosition() {
		this.centeredPositionSyncProperty.setChanged();
	}

	/**
	 * Don't call this directly. Use
	 * {@link MoveOnMapService#unsetPath(IF_MapMoveable)} instead.
	 */
	@Override
	public void unsetPath() {
		this.pathPositionSyncProperty.setValue(null);
	}

	@Override
	public IF_Position_ImmutableView getCenteredPosition() {
		return this.centeredPositionSyncProperty.getValue();
	}

	public void setCenteredPosition(IF_Position_ImmutableView newPosition) {
		this.centeredPositionSyncProperty.getValue().setFX(newPosition.getFX());
		this.centeredPositionSyncProperty.getValue().setFY(newPosition.getFY());
	}

	@Override
	public void move(Orientation orientation, float distanceInPixel) {
		XY orientationFactorOptimized = orientation.orientationFactorOptimized;
		float fx = this.centeredPositionSyncProperty.getValue().getFX();
		this.centeredPositionSyncProperty.getValue().setFX(fx + orientationFactorOptimized.getFX() * distanceInPixel);
		float fy = this.centeredPositionSyncProperty.getValue().getFY();
		this.centeredPositionSyncProperty.getValue().setFY(fy + orientationFactorOptimized.getFY() * distanceInPixel);
		this.setOrientationWithoutSync(orientation);
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
		PathPosition pathPosition = this.pathPositionSyncProperty.getValue();
		if (pathPosition != null) {
			return pathPosition.getPath();
		} else {
			return null;
		}
	}

	public Orientation getOrientation() {
		return this.renderableKeySyncProperty.getValue().orientation;
	}

	@Override
	public PathPosition getPathPosition() {
		return this.pathPositionSyncProperty.getValue();
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
		this.renderableKeySyncProperty.change(renderableKey -> renderableKey.orientation = orientation);
	}

	public void setOrientationWithoutSync(Orientation orientation) {
		this.renderableKeySyncProperty.getValue().orientation = orientation;
	}

	public void setEntityKey(IF_EntityType entityType) {
		this.renderableKeySyncProperty.change(renderableKey -> renderableKey.entityType = entityType);
	}

	public void setAnimationKeyWithoutSync(IF_AnimationKey animationKey) {
		this.renderableKeySyncProperty.getValue().animationKey = animationKey;
	}

	public void setAnimationKey(IF_AnimationKey animationKey) {
		this.renderableKeySyncProperty.change(renderableKey -> renderableKey.animationKey = animationKey);
	}

	public RenderableKey getRenderableKey() {
		return this.renderableKeySyncProperty.getValue();
	}

	@Override
	public float getSpeed() {
		Float speed = this.speedPerTickSyncProperty.getValue();
		return speed != null ? speed : 0f;
	}

	public Container getContainerHas() {
		return this.containerHas;
	}

	public Container getContainerNeeds() {
		return this.containerNeeds;
	}

	public Container getContainerProvides() {
		return this.containerProvides;
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public String getText() {
		// if (this.ki != null) {
		// return this.ki.currentActiveKI().getClass().getName();
		// } else {
		return this.getOrientation().name() + " vs " + this.textSyncProperty.getValue();
		// }

		// return this.text;
	}

	public void setText(String text) {
		this.textSyncProperty.setValue(text);
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
