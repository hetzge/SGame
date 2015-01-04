package de.hetzge.sgame.render;

import java.io.Serializable;

public class DynamicRenderableKey implements IF_RenderableKey, Serializable {

	public static final DynamicRenderableKey DEFAULT_DYNAMIC_RENDERABLE_KEY = new DynamicRenderableKey("");

	private String key;

	public DynamicRenderableKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof DynamicRenderableKey))
			return false;
		if (obj == this)
			return true;
		DynamicRenderableKey tileAnimationKey = (DynamicRenderableKey) obj;
		return tileAnimationKey.key.equals(this.key);
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public String toString() {
		return "DynamicRenderableKey -> " + this.getKey();
	}

}