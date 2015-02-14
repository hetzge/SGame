package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.Color;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableRectangle implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		context.shapeRenderer.setColor(Color.CYAN);
		IF_Rectangle_ImmutableView rectangle = onScreen.getRenderedRectangle();

		IF_Position_ImmutableView positionA = rectangle.getPositionA();
		IF_Dimension_ImmutableView dimension = rectangle.getDimension();

		context.shapeRenderer.rect(positionA.getFX(), positionA.getFY(), dimension.getWidth(), dimension.getHeight());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}

}
