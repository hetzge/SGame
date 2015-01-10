package de.hetzge.sgame.render;

import java.util.LinkedList;
import java.util.List;

public class RenderPool implements IF_Renderable<IF_RenderableContext> {

	private final List<IF_Renderable<IF_RenderableContext>> toRenderComponents = new LinkedList<>();

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
