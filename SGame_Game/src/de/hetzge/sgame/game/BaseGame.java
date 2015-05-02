package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.Log;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.entity.EntityFactory;
import de.hetzge.sgame.entity.EntityModule;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.ki.EntityKI;
import de.hetzge.sgame.entity.ki.KIModule;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.map.MapModule;
import de.hetzge.sgame.message.MessageModule;
import de.hetzge.sgame.network.NetworkConfig;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.network.PeerRole;
import de.hetzge.sgame.sync.SyncConfig;
import de.hetzge.sgame.sync.SyncModule;

public class BaseGame extends Application {

	protected final MapModule mapModule;
	protected final NetworkModule networkModule;
	protected final SyncModule syncModule;
	protected final SyncConfig syncConfig;
	protected final EntityModule entityModule;
	protected final MessageModule messageModule;
	protected final NetworkConfig networkConfig;
	protected final EntityFactory entityFactory;
	protected final EntityPool entityPool;
	protected final EntityOnMapService onMapService;
	protected final KIModule kiModule;

	// TODO singleplayer, client, server in AppConfig oder sowas packen
	protected final boolean singleplayer;
	protected final boolean client;
	protected final boolean server;

	public BaseGame(boolean singleplayer, boolean client, boolean server, Class<? extends BootstrapperBundle> bootstrapperBundle) {
		super(bootstrapperBundle);

		Stopwatch stopwatch = new Stopwatch("Init BaseGame");

		this.singleplayer = singleplayer;
		this.client = client;
		this.server = server;

		Log.LOG.info("singleplayer: " + singleplayer);
		Log.LOG.info("client: " + client);
		Log.LOG.info("server: " + server);

		this.mapModule = this.get(MapModule.class);
		this.networkModule = this.get(NetworkModule.class);
		this.networkConfig = this.get(NetworkConfig.class);
		this.syncModule = this.get(SyncModule.class);
		this.syncConfig = this.get(SyncConfig.class);
		this.entityModule = this.get(EntityModule.class);
		this.entityFactory = this.get(EntityFactory.class);
		this.entityPool = this.get(EntityPool.class);
		this.messageModule = this.get(MessageModule.class);
		this.onMapService = this.get(EntityOnMapService.class);
		this.kiModule = this.get(KIModule.class);

		if (client && server) {
			throw new IllegalStateException("at the moment only client or server is possible");
		}

		if (client || server) {
			if (client) {
				this.networkConfig.peerRole = PeerRole.CLIENT;
			} else {
				this.networkConfig.peerRole = PeerRole.SERVER;
			}
			this.modulePool.registerModules(this.networkModule, this.syncModule, this.messageModule, this.kiModule);
		}

		this.modulePool.registerModules(this.mapModule, this.entityModule);

		this.entityFactory.registerFactory(EntityType.SILLY_BLOCK, (entity) -> {
			entity.setEntityKey(EntityType.SILLY_BLOCK);
			entity.setOrientation(Orientation.SOUTH);
			entity.setRealDimension(new XY(10f, 10f));
			entity.setDimension(new XY(40f, 40f));
			entity.setCollision(new boolean[][] { new boolean[] { true }});
			entity.setKI(new EntityKI(entity));

			entity.getContainerHas().setMax(Definition.Item.WOOD, 10);
		});

		this.entityFactory.registerFactory(EntityType.TREE, (entity) -> {
			entity.setEntityKey(EntityType.TREE);
			entity.setDimension(new XY(64f, 64f));
			entity.setRealDimension(new XY(40f, 40f));
			entity.setFixedPosition(true);
			entity.setCollision(this.onMapService.on(entity.getRealRectangle()).asCollisionArray());

			entity.getContainerProvides().set(Definition.Item.WOOD, 100);
		});

		stopwatch.stop();
	}

}
