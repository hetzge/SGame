package de.hetzge.sgame.map;

import de.hetzge.sgame.render.IF_RenderableContext;

public class MapConfig {

	public static final MapConfig INSTANCE = new MapConfig();

	public final int collisionTileFacor = 3;

	public TileMap<IF_RenderableContext> tileMap = new TileMap<IF_RenderableContext>(100, 100, 32);

	private MapConfig() {
	}

}
