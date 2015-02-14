package de.hetzge.sgame.common.definition;

import java.io.Serializable;

import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

public interface IF_RenderInformation extends Serializable {
	public IF_Rectangle_ImmutableView getRenderedRectangle();

	public default int getRenderId() {
		return 0;
	}
}
