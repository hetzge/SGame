package de.hetzge.sgame.common.newgeometry.views;

import java.io.Serializable;

public interface IF_Rectangle_View extends Serializable {

	public IF_Rectangle_ImmutableView asRectangleImmutableView();

	public IF_Rectangle_MutableView asRectangleMutableView();

}
