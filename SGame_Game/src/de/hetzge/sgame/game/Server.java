package de.hetzge.sgame.game;

import java.util.Set;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.network.NetworkConfig;
import de.hetzge.sgame.network.PeerRole;

public class Server extends BaseGame {

	public Server() {
		super();
		NetworkConfig.INSTANCE.peerRole = PeerRole.SERVER;

		for (int i = 0; i < 100; i++) {
			EntityConfig.INSTANCE.entityFactory.build(EntityType.SILLY_BLOCK, (entity) -> {
				PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);
				module.setPosition(new Position((float) Math.random() * 300, (float) Math.random() * 300));
			});
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	@Override
	public void update() {
		super.update();
		Set<Entity> entitiesByType = EntityConfig.INSTANCE.entityPool.getEntitiesByType(EntityType.SILLY_BLOCK);
		for (Entity entity : entitiesByType) {
			PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);

			double random = Math.random();
			if (random > 0.75d) {
				module.move(Orientation.EAST, 0.5f);
			} else if (random > 0.5d) {
				module.move(Orientation.SOUTH, 0.5f);
			} else if (random > 0.25) {
				module.move(Orientation.NORTH, 0.5f);
			} else {
				module.move(Orientation.WEST, 0.5f);
			}
		}
	}

}
