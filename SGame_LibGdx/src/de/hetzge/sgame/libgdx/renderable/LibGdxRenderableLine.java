package de.hetzge.sgame.libgdx.renderable;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry2.IF_Dimension_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Rectangle_Immutable;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableLine implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		IF_Rectangle_Immutable rectangle = onScreen.getRenderedRectangle();

		IF_Position_Immutable positionA = rectangle.getA();
		IF_Dimension_Immutable dimension = rectangle.getDimension();

		// TODO geht nur positive da dimension verwendet wird

		context.shapeRenderer.line(positionA.getFX(), positionA.getFY(), dimension.getWidth(), dimension.getHeight());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}
}
