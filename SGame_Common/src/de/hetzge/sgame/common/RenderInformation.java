package de.hetzge.sgame.common;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

public class RenderInformation implements IF_RenderInformation {

	public IF_Rectangle_ImmutableView rectangle;
	public int renderId;

	public RenderInformation() {
	}

	@Override
	public IF_Rectangle_ImmutableView getRenderedRectangle() {
		return this.rectangle;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}

}