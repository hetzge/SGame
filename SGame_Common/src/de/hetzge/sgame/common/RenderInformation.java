package de.hetzge.sgame.common;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry2.IF_Rectangle_Immutable;

public class RenderInformation implements IF_RenderInformation {

	public IF_Rectangle_Immutable rectangle;
	public int renderId;

	public RenderInformation() {
	}

	@Override
	public IF_Rectangle_Immutable getRenderedRectangle() {
		return this.rectangle;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}

}