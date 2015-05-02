package de.hetzge.sgame.game;

import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.render.RenderModule;

public class Server extends Client {

	public Server(boolean singleplayer) {
		super(singleplayer, false, true, ServerBootstrapperBundle.class);

		Stopwatch stopwatch = new Stopwatch("Init Server");

		for (int i = 0; i < 10; i++) {
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

		stopwatch.stop();
	}

	@Override
	public void update() {
		super.update();
	}

	public static void main(String[] args) {
		boolean isSingleplayer = Server.checkArgsForIsSingleplayer(args);

		Server server = new Server(isSingleplayer);
		server.start();
	}

	protected static boolean checkArgsForIsSingleplayer(String[] args) {
		boolean singleplayer = false;
		if (args != null && args.length > 0) {
			singleplayer = Boolean.valueOf(args[0]);
		}
		return singleplayer;
	}

}
