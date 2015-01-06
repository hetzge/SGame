package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.activemap.ActiveMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;

public class CommonConfig {

	private class DummyMap implements IF_Map {
		@Override
		public int getWidthInTiles() {
			return 100;
		}

		@Override
		public float getWidthInPx() {
			return 3200;
		}

		@Override
		public float getTileSize() {
			return 32;
		}

		@Override
		public int getHeightInTiles() {
			return 100;
		}

		@Override
		public float getHeightInPx() {
			return 3200;
		}

		@Override
		public int getCollisionTileFactor() {
			return 3;
		}

		@Override
		public IF_Collision getCollision() {
			return null;
		}

		@Override
		public ActiveMap<Boolean> getFixEntityCollisionMap() {
			return null;
		}

		@Override
		public ActiveMap<Boolean> getFlexibleEntityCollisionMap() {
			return null;
		}
	}

	public static final CommonConfig INSTANCE = new CommonConfig();

	public final FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();

	public IF_Map map = new DummyMap();

	private CommonConfig() {
	}

}
