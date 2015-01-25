package de.hetzge.sgame.entity.module;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.common.geometry.ComplexRectangle;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.render.IF_AnimationKey;
import de.hetzge.sgame.render.RenderableKey;
import de.hetzge.sgame.sync.SyncProperty;

public class RenderableModule extends BaseEntityModule {

	private final SyncProperty<RenderableKey> renderableKeySyncProperty = this.createSyncProperty(new RenderableKey());

	public RenderableModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
	}

	@Override
	public void updateImpl() {
	}

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

	public IF_ImmutableComplexRectangle<?, ?> getRenderedRectangle() {
		PositionAndDimensionModule positionAndDimensionModule = this.entity.getModule(PositionAndDimensionModule.class);
		if (positionAndDimensionModule != null) {
			return positionAndDimensionModule.getPositionAndDimensionRectangle();
		} else {
			return new ComplexRectangle();
		}
	}

	public RenderableKey getRenderableKey() {
		return this.renderableKeySyncProperty.getValue();
	}

}
