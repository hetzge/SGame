package de.hetzge.sgame.game;

import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.newgeometry2.XY;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.render.RenderModule;

public class Server extends Client {

	public Server(boolean singleplayer) {
		super(singleplayer, false, true, ServerBootstrapperBundle.class);

		Stopwatch stopwatch = new Stopwatch("Init Server");

		for (int x = 0; x < 3; x++) {
			int ix = 3 + x;
			for (int y = 0; y < 3; y++) {
				int iy = 3 + y;
				this.entityFactory.build(EntityType.SILLY_BLOCK, (entity) -> {
					entity.setCenteredPosition(new XY(ix * 20, iy * 20));
					// module.set(new Position((float) Math.random() * 1000,
					// (float)
					// Math.random() * 800), 50000);
				});
			}
		}

		for (int i = 0; i < 20; i++) {
			XY xy = new XY(200f + i * 48, 500f);
			this.entityFactory.build(EntityType.TREE, (entity) -> {
				entity.setPositionA(xy);
			});
		}

		for (int i = 0; i < 20; i++) {
			XY xy = new XY(200f + i * 48, 800f);
			this.entityFactory.build(EntityType.TREE, (entity) -> {
				entity.setPositionA(xy);
			});
		}


		// for (int i = 0; i < 50; i++) {
		// this.entityFactory.build(EntityType.TREE, (entity) -> {
		// entity.setPosition(new XY((float) Math.random() * 1000 + 200, (float)
		// Math.random() * 1000 + 200));
		// });
		// }

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
