package de.hetzge.sgame.common;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;

public class RenderInformation implements IF_RenderInformation {
	public IF_ImmutablePrimitivRectangle rectangle;
	public int renderId;

	public RenderInformation() {
	}

	@Override
	public IF_ImmutablePrimitivRectangle getRenderedRectangle() {
		return this.rectangle;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}

}