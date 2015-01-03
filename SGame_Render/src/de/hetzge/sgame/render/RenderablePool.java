package de.hetzge.sgame.render;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RenderablePool implements IF_Renderable<IF_RenderableContext> {

	private final Map<IF_RenderableKey, IF_RenderableWrapper<?>> renderableRessources = new HashMap<>();
	private final List<IF_Renderable<IF_RenderableContext>> toRenderComponents = new LinkedList<>();

	public RenderablePool() {
	}

	public void registerRenderableRessource(IF_RenderableKey renderableKey, IF_RenderableWrapper<?> renderable) {
		this.renderableRessources.put(renderableKey, renderable);
	}

	public IF_RenderableWrapper<?> getRenderableRessource(IF_RenderableKey renderableKey) {
		return this.renderableRessources.get(renderableKey);
	}

	public void registerComponentToRender(IF_Renderable<IF_RenderableContext> renderable) {
		this.toRenderComponents.add(renderable);
	}

	@Override
	public void render(IF_RenderableContext context) {
		for (IF_Renderable<IF_RenderableContext> if_Renderable : this.toRenderComponents) {
			if_Renderable.render(context);
		}
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		for (IF_Renderable<IF_RenderableContext> if_Renderable : this.toRenderComponents) {
			if_Renderable.renderShapes(context);
		}
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		for (IF_Renderable<IF_RenderableContext> if_Renderable : this.toRenderComponents) {
			if_Renderable.renderFilledShapes(context);
		}
	}

}
