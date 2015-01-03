package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.definition.IF_Map;

public class CommonConfig {

	public static final CommonConfig INSTANCE = new CommonConfig();

	public final FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();

	public final float tileSize = 32;
	public final int collisionTileFactor = 3;
	public final float collisionTileSize = this.tileSize / 3;

	public IF_Map map;

	private CommonConfig() {
	}

}
