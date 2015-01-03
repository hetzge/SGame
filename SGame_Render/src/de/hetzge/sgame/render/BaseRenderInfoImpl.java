package de.hetzge.sgame.render;

import java.io.Serializable;

import de.hetzge.sgame.common.geometry.Rectangle;

public class BaseRenderInfoImpl implements IF_RenderInformation, Serializable {

	private final Rectangle rectangle;
	private final IF_RenderableKey renderableKey;

	public BaseRenderInfoImpl(Rectangle rectangle, IF_RenderableKey renderableKey) {
		this.rectangle = rectangle;
		this.renderableKey = renderableKey;
	}

	@Override
	public Rectangle getRenderedRectangle() {
		return this.rectangle;
	}

	@Override
	public IF_RenderableKey getRenderableKey() {
		return this.renderableKey;
	}

}
