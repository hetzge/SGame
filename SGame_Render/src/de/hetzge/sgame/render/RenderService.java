package de.hetzge.sgame.render;

import de.hetzge.sgame.common.RenderInformation;
import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;

public final class RenderService {

	private volatile static int nextRenderId = 0;

	public static int renderCount = 0;

	private final Viewport viewport;
	private final RenderableRessourcePool renderableRessourcePool;
	private final RenderableIdPool renderableIdPool;

	// for reuse
	private final RenderInformation renderInformation = new RenderInformation();

	public RenderService(Viewport viewport, RenderableRessourcePool renderableRessourcePool, RenderableIdPool renderableIdPool) {
		this.viewport = viewport;
		this.renderableRessourcePool = renderableRessourcePool;
		this.renderableIdPool = renderableIdPool;
	}

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_ImmutablePrimitivRectangle rectangle, int renderId) {
		this.renderInformation.rectangle = rectangle;
		this.renderInformation.renderId = renderId;
		this.render(context, this.renderInformation);
	}

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_ImmutablePrimitivRectangle rectangle, RenderableKey renderableKey) {
		this.renderInformation.rectangle = rectangle;
		this.renderInformation.renderId = this.renderableIdPool.get(renderableKey);
		this.render(context, this.renderInformation);
	}

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_RenderInformation onScreen) {
		IF_ImmutablePrimitivRectangle renderedRectangle = onScreen.getRenderedRectangle();
		if (this.viewport.doesOverlapWith(renderedRectangle)) {
			IF_RenderableWrapper<CONTEXT> renderable = (IF_RenderableWrapper<CONTEXT>) this.renderableRessourcePool.getRenderableRessource(onScreen.getRenderId());
			if (renderable == null) {
				throw new IllegalStateException("No renderable for key " + onScreen.getRenderId());
			}
			renderable.render(context, onScreen);
			RenderService.renderCount++;
		}
	}

	public static int getNextRenderId() {
		return RenderService.nextRenderId++;
	}

}
