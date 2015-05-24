package de.hetzge.sgame.render;

import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Rectangle_Immutable;

public interface IF_DrawService {

	void drawLine(IF_Position_Immutable from, IF_Position_Immutable to);

	void drawRectangle(IF_Rectangle_Immutable rectangle);

	void printText(IF_Position_Immutable position, String text);

}
