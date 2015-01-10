package de.hetzge.sgame.map;

import net.openhft.koloboke.collect.map.hash.HashIntIntMap;
import net.openhft.koloboke.collect.map.hash.HashIntIntMaps;

public class TilePool {

	private HashIntIntMap tileIdToRenderableId = HashIntIntMaps.newMutableMap();

	public void map(int tileId, int renderableId) {
		this.tileIdToRenderableId.put(tileId, renderableId);
	}

	public int getRenderableId(int tileId) {
		return this.tileIdToRenderableId.get(tileId);
	}

}
