package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.Color;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableRectangle implements IF_RenderableWrapper<LibGdxRenderableContext> {

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		context.shapeRenderer.setColor(Color.CYAN);
		IF_ImmutablePrimitivRectangle rectangle = onScreen.getRenderedRectangle();
		context.shapeRenderer.rect(rectangle.getAX(), rectangle.getAY(), rectangle.getWidth(), rectangle.getHeight());
	}

	@Override
	public Object getNativeObject() {
		throw new UnsupportedOperationException();
	}

}
