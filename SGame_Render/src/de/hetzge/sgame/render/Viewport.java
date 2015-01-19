package de.hetzge.sgame.render;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.common.IF_XYFunction;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.PrimitivRectangle;

public class Viewport extends PrimitivRectangle {

	public void iterateVisibleTiles(IF_XYFunction<Void> function) {
		IF_Map map = CommonConfig.INSTANCE.map;

		int startX = map.convertPxInTile(RenderConfig.INSTANCE.viewport.getAX());
		int startY = map.convertPxInTile(RenderConfig.INSTANCE.viewport.getAY());
		int endX = startX + map.convertPxInTile(RenderConfig.INSTANCE.viewport.getWidth()) + 1;
		int endY = startY + map.convertPxInTile(RenderConfig.INSTANCE.viewport.getHeight()) + 1;
		if (startX < 0) {
			startX = 0;
		}
		if (startY < 0) {
			startY = 0;
		}
		if (endX > map.getWidthInTiles()) {
			endX = map.getWidthInTiles();
		}
		if (endY > map.getHeightInTiles()) {
			endY = map.getHeightInTiles();
		}

		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {
				function.on(x, y);
			}
		}
	}

}
