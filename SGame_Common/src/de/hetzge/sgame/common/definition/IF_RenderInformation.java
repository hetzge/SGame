package de.hetzge.sgame.common.definition;

import java.io.Serializable;

import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;

public interface IF_RenderInformation extends Serializable {
	public IF_ImmutablePrimitivRectangle getRenderedRectangle();

	public default int getRenderableKey() {
		return 0; // TODO new default
	}
}
