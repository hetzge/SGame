package de.hetzge.sgame.render;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.IF_XYFunction;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.newgeometry2.IF_Dimension_Immutable;
import de.hetzge.sgame.common.newgeometry2.IF_Position_Immutable;
import de.hetzge.sgame.common.newgeometry2.Rectangle;

public class Viewport extends Rectangle {

	private final IF_MapProvider mapProvider;

	public Viewport(IF_MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	public void iterateVisibleTiles(IF_XYFunction<Void> function) {
		IF_Map map = this.mapProvider.provide();

		IF_Position_Immutable positionA = this.getA();
		IF_Dimension_Immutable dimension = this.getDimension();

		int startX = map.convertPxInTile(positionA.getFX()) - 1;
		int startY = map.convertPxInTile(positionA.getFY()) - 1;
		int endX = startX + map.convertPxInTile(dimension.getWidth()) + 3;
		int endY = startY + map.convertPxInTile(dimension.getHeight()) + 3;
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
