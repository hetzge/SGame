package de.hetzge.sgame.common.newgeometry;

import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_MutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_View;

public interface IF_Rectangle extends IF_Rectangle_View, IF_Rectangle_MutableView {

	@Override
	public default IF_Rectangle_ImmutableView asRectangleImmutableView() {
		return this;
	}

	@Override
	public default IF_Rectangle_MutableView asRectangleMutableView() {
		return this;
	}

}
