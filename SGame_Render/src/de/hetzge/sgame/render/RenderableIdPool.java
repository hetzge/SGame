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
		return this.renderableKeyToRenderableId.get(renderableKey);
	}

	public int get(String dynamicRenderableKey) {
		return this.dynamicRenderableKeyToRenderableId.get(dynamicRenderableKey);
	}

}