package de.hetzge.sgame.render;

import de.hetzge.sgame.common.geometry.Rectangle;

public final class RenderUtil {

	private RenderUtil() {
	}

	public static <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_RenderInformation onScreen) {
		Rectangle renderedRectangle = onScreen.getRenderedRectangle();
		if (RenderConfig.INSTANCE.viewport.doesOverlapWith(renderedRectangle)) {
			IF_RenderableWrapper<CONTEXT> renderable = (IF_RenderableWrapper<CONTEXT>) RenderConfig.INSTANCE.renderablePool.getRenderableRessource(onScreen.getRenderableKey());
			if (renderable == null) {
				throw new IllegalStateException("No renderable for key " + onScreen.getRenderableKey());
			}
			renderable.render(context, onScreen);
		}
	}

}
