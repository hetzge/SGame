package de.hetzge.sgame.libgdx.renderable;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableLine implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		IF_Rectangle_ImmutableView rectangle = onScreen.getRenderedRectangle();

		IF_Position_ImmutableView positionA = rectangle.getPositionA();
		IF_Position_ImmutableView positionD = rectangle.getPositionD();

		context.shapeRenderer.line(positionA.getFX(), positionA.getFY(), positionD.getFX(), positionD.getFY());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}
}
