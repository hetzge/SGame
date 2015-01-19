package de.hetzge.sgame.render;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;

public final class RenderUtil {

	private volatile static int nextRenderId = 0;

	public static int renderCount = 0;

	private RenderUtil() {
	}

	public static <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_RenderInformation onScreen) {
		IF_ImmutablePrimitivRectangle renderedRectangle = onScreen.getRenderedRectangle();
		if (RenderConfig.INSTANCE.viewport.doesOverlapWith(renderedRectangle)) {
			IF_RenderableWrapper<CONTEXT> renderable = (IF_RenderableWrapper<CONTEXT>) RenderConfig.INSTANCE.renderableRessourcePool.getRenderableRessource(onScreen.getRenderableKey());
			if (renderable == null) {
				throw new IllegalStateException("No renderable for key " + onScreen.getRenderableKey());
			}
			renderable.render(context, onScreen);
			RenderUtil.renderCount++;
		}
	}

	public static int getNextRenderId() {
		return RenderUtil.nextRenderId++;
	}

}
