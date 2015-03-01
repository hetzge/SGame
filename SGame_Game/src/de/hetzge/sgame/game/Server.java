package de.hetzge.sgame.game;

import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.entity.ki.KIModule;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.network.PeerRole;
import de.hetzge.sgame.render.RenderModule;

public class Server extends BaseGame {

	public Server() {
		super(ServerBootstrapperBundle.class);
		this.networkConfig.peerRole = PeerRole.SERVER;

		for (int i = 0; i < 1; i++) {
			this.entityFactory.build(EntityType.SILLY_BLOCK, (entity) -> {
				entity.setCenteredPosition(new XY((float) Math.random() * 1000 + 100, (float) Math.random() * 1000 + 100));
				// module.set(new Position((float) Math.random() * 1000, (float)
				// Math.random() * 800), 50000);
			});
		}

		this.entityFactory.build(EntityType.TREE, (entity) -> {
			entity.setPositionA(new XY(500f, 500f));
		});

		//				for (int i = 0; i < 50; i++) {
		//					this.entityFactory.build(EntityType.TREE, (entity) -> {
		//						entity.setPosition(new XY((float) Math.random() * 1000 + 200, (float) Math.random() * 1000 + 200));
		//					});
		//				}

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
