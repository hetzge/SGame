package de.hetzge.sgame.render;

import de.hetzge.sgame.common.geometry.Rectangle;

public interface IF_RenderInformation {

	public Rectangle getRenderedRectangle();

	public default IF_RenderableKey getRenderableKey() {
		return IF_RenderableKey.DEFAULT_RENDERABLE_KEY;
	}
}
