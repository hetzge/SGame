package de.hetzge.sgame.render;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.IF_XYFunction;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.geometry.PrimitivRectangle;

public class MapViewport extends PrimitivRectangle {

	private final IF_MapProvider mapProvider;

	public MapViewport(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	public void iterateVisibleTiles(IF_XYFunction<Void> function) {
		IF_Map map = this.mapProvider.provide();

		int startX = map.convertPxInTile(this.getAX()) - 1;
		int startY = map.convertPxInTile(this.getAY()) - 1;
		int endX = startX + map.convertPxInTile(this.getWidth()) + 3;
		int endY = startY + map.convertPxInTile(this.getHeight()) + 3;
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
