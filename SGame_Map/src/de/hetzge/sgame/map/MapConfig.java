package de.hetzge.sgame.map;

import de.hetzge.sgame.common.CommonConfig;
import de.hetzge.sgame.render.IF_RenderableContext;

public class MapConfig {

	public static final MapConfig INSTANCE = new MapConfig();

	private TileMap<IF_RenderableContext> tileMap;
	public final TilePool tilePool = new TilePool();

	public TileMap<IF_RenderableContext> getTileMap() {
		return this.tileMap;
	}

	public void setTileMap(TileMap<IF_RenderableContext> tileMap) {
		this.tileMap = tileMap;
		CommonConfig.INSTANCE.map = tileMap;
	}

	private MapConfig() {
	}

}
