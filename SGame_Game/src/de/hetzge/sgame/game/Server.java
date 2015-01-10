package de.hetzge.sgame.game;

import de.hetzge.sgame.application.ApplicationConfig;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.entity.ki.KIModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.network.NetworkConfig;
import de.hetzge.sgame.network.PeerRole;

public class Server extends BaseGame {

	public Server() {
		super();
		NetworkConfig.INSTANCE.peerRole = PeerRole.SERVER;

		for (int i = 0; i < 10; i++) {
			EntityConfig.INSTANCE.entityFactory.build(EntityType.SILLY_BLOCK, (entity) -> {
				PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);
				module.setPosition(new InterpolatePosition(new Position((float) Math.random() * 300, (float) Math.random() * 300)));
				// module.set(new Position((float) Math.random() * 1000, (float)
				// Math.random() * 800), 50000);
				});
		}

		ApplicationConfig.INSTANCE.modulePool.registerModules(new KIModule());
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	@Override
	public void update() {
		super.update();
	}

}
