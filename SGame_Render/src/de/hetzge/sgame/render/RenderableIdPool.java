package de.hetzge.sgame.render;

import java.util.HashMap;
import java.util.Map;

public class RenderableIdPool {
	private final Map<RenderableKey, Integer> renderableKeyToRenderableId = new HashMap<>();
	private final Map<String, Integer> dynamicRenderableKeyToRenderableId = new HashMap<>();

	public void register(RenderableKey renderableKey, int renderableId) {
		this.renderableKeyToRenderableId.put(renderableKey, renderableId);
	}

	public void register(String dynamicRenderableKey, int renderableId) {
		this.dynamicRenderableKeyToRenderableId.put(dynamicRenderableKey, renderableId);
	}

	public int get(RenderableKey renderableKey) {
		try {
			return this.renderableKeyToRenderableId.get(renderableKey);
		} catch (Exception e) {
			throw new IllegalStateException("No renderable id for: " + renderableKey, e);
		}
	}

	public int get(String dynamicRenderableKey) {
		return this.dynamicRenderableKeyToRenderableId.get(dynamicRenderableKey);
	}

}