package de.hetzge.sgame.render;

import de.hetzge.sgame.common.RenderInformation;
import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

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

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_Rectangle_ImmutableView rectangle, int renderId) {
		this.renderInformation.rectangle = rectangle;
		this.renderInformation.renderId = renderId;
		this.render(context, this.renderInformation);
	}

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_Rectangle_ImmutableView rectangle, RenderableKey renderableKey) {
		this.renderInformation.rectangle = rectangle;
		this.renderInformation.renderId = this.renderableIdPool.get(renderableKey);
		this.render(context, this.renderInformation);
	}

	public <CONTEXT extends IF_RenderableContext> void render(CONTEXT context, IF_RenderInformation renderInformation) {
		IF_Rectangle_ImmutableView renderedRectangle = renderInformation.getRenderedRectangle();
		if (this.viewport.doesOverlapWith(renderedRectangle)) {
			IF_RenderableWrapper<CONTEXT> renderable = (IF_RenderableWrapper<CONTEXT>) this.renderableRessourcePool.getRenderableRessource(renderInformation.getRenderId());
			if (renderable == null) {
				throw new IllegalStateException("No renderable for key " + renderInformation.getRenderId());
			}
			renderable.render(context, renderInformation);
			RenderService.renderCount++;
		}
	}

	public static int getNextRenderId() {
		return RenderService.nextRenderId++;
	}

}
