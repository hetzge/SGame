package de.hetzge.sgame.map;

import se.jbee.inject.bind.BinderModule;
import de.hetzge.sgame.common.IF_MapProvider;

public class MapBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(MapConfig.class).to(MapConfig.class);
		this.bind(MapModule.class).to(MapModule.class);
		this.bind(TilePool.class).to(TilePool.class);
		this.bind(IF_MapProvider.class).to(TileMapProvider.class);
	}

}
