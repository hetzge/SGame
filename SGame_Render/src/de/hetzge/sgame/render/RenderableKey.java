package de.hetzge.sgame.render;

import java.io.Serializable;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.definition.IF_EntityType;

public class RenderableKey implements IF_RenderableKey, Serializable {

	private static enum DefaultAnimationKey implements IF_AnimationKey, Serializable {
		DEFAULT;
	}

	private static enum DefaultEntitiyType implements IF_EntityType, Serializable {
		DEFAULT;
	}

	public IF_AnimationKey animationKey = DefaultAnimationKey.DEFAULT;
	public IF_EntityType entityType = DefaultEntitiyType.DEFAULT;
	public Orientation orientation = Orientation.DEFAULT;

	public RenderableKey(IF_AnimationKey animationKey, IF_EntityType entityType, Orientation orientation) {
		this.animationKey = animationKey;
		this.entityType = entityType;
		this.orientation = orientation;
	}

	public RenderableKey() {
	}

	public RenderableKey animationKey(IF_AnimationKey animationKey) {
		this.animationKey = animationKey;
		return this;
	}

	public RenderableKey entityType(IF_EntityType entityType) {
		this.entityType = entityType;
		return this;
	}

	public RenderableKey orientation(Orientation orientation) {
		this.orientation = orientation;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.animationKey == null) ? 0 : this.animationKey.hashCode());
		result = prime * result + ((this.entityType == null) ? 0 : this.entityType.hashCode());
		result = prime * result + ((this.orientation == null) ? 0 : this.orientation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		RenderableKey other = (RenderableKey) obj;
		if (this.animationKey == null) {
			if (other.animationKey != null)
				return false;
		} else if (!this.animationKey.equals(other.animationKey))
			return false;
		if (this.entityType == null) {
			if (other.entityType != null)
				return false;
		} else if (!this.entityType.equals(other.entityType))
			return false;
		if (this.orientation != other.orientation)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RenderableKey -> " + this.animationKey.toString() + " " + this.orientation.toString() + " " + this.entityType.toString();
	}

}
