package de.hetzge.sgame.libgdx.renderable;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableLine implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		IF_ImmutablePrimitivRectangle rectangle = onScreen.getRenderedRectangle();
		context.shapeRenderer.line(rectangle.getAX(), rectangle.getAY(), rectangle.getDX(), rectangle.getDY());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}
}
