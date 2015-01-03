package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.Color;

import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableRectangle implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		context.shapeRenderer.setColor(Color.CYAN);
		Rectangle renderedRectangle = onScreen.getRenderedRectangle();
		context.shapeRenderer.rect(renderedRectangle.getStartPosition().getX(), renderedRectangle.getStartPosition().getY(), renderedRectangle.getDimension().getWidth(), renderedRectangle
				.getDimension().getHeight());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}

}
