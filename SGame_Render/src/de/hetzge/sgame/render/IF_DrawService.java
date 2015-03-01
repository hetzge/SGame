package de.hetzge.sgame.render;

import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;

public interface IF_DrawService {

	void drawLine(IF_Position_ImmutableView from, IF_Position_ImmutableView to);

	void drawRectangle(IF_Rectangle_ImmutableView rectangle);

	void printText(IF_Position_ImmutableView position, String text);

}
