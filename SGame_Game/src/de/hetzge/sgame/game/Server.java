package de.hetzge.sgame.game;

import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.entity.ki.KIModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.network.PeerRole;
import de.hetzge.sgame.render.RenderModule;

public class Server extends BaseGame {

	public Server() {
		super(ServerBootstrapperBundle.class);
		this.networkConfig.peerRole = PeerRole.SERVER;

		for (int i = 0; i < 10; i++) {
			this.entityFactory.build(EntityType.SILLY_BLOCK, (entity) -> {
				PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);
				module.setPosition(new InterpolatePosition(new Position((float) Math.random() * 100 + 100, (float) Math.random() * 100 + 100)));
				// module.set(new Position((float) Math.random() * 1000, (float)
				// Math.random() * 800), 50000);
				});
		}

		for (int i = 0; i < 1000; i++) {
			this.entityFactory.build(EntityType.TREE, (entity) -> {
				PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);
				module.setPosition(new InterpolatePosition(new Position((float) Math.random() * 1000 + 200, (float) Math.random() * 1000 + 200)));
			});
		}

		// TODO Server sollte keine Render abhängigkeit haben ?!
		this.modulePool.registerModule(this.get(RenderModule.class));
		this.modulePool.registerModule(this.get(KIModule.class));
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
