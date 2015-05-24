package de.hetzge.sgame.common.definition;

import java.io.Serializable;

import de.hetzge.sgame.common.newgeometry2.IF_Rectangle_Immutable;

public interface IF_RenderInformation extends Serializable {
	public IF_Rectangle_Immutable getRenderedRectangle();

	public default int getRenderId() {
		return 0;
	}
}
