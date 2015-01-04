package de.hetzge.sgame.render;

import java.io.Serializable;

import de.hetzge.sgame.common.geometry.IF_ImmutableRectangle;

public interface IF_RenderInformation extends Serializable {

	public IF_ImmutableRectangle getRenderedRectangle();

	public default IF_RenderableKey getRenderableKey() {
		return IF_RenderableKey.DEFAULT_RENDERABLE_KEY;
	}
}
