package de.hetzge.sgame.common.newgeometry2;

public interface IF_XY_Mutable extends IF_XY {
	XY add(IF_XY xy);

	XY minus(IF_XY xy);

	XY divide(IF_XY xy);

	XY multiply(IF_XY xy);

	XY set(IF_XY xy);

	XY abs();
}
