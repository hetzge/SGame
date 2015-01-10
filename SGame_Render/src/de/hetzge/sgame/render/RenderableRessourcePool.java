package de.hetzge.sgame.render;

import java.util.HashMap;
import java.util.Map;

public class RenderableRessourcePool {

	private final Map<Integer, IF_RenderableWrapper<?>> renderableRessources = new HashMap<>();

	public RenderableRessourcePool() {
	}

	public void registerRenderableRessource(int renderableId, IF_RenderableWrapper<?> renderable) {
		this.renderableRessources.put(renderableId, renderable);
	}

	public void registerRenderableRessource(RenderableKey renderableKey, IF_RenderableWrapper<?> renderable) {
		int nextRenderId = RenderUtil.getNextRenderId();
		RenderConfig.INSTANCE.renderableIdPool.register(renderableKey, nextRenderId);
		this.registerRenderableRessource(nextRenderId, renderable);
	}

	public IF_RenderableWrapper<?> getRenderableRessource(int renderableId) {
		return this.renderableRessources.get(renderableId);
	}

}
