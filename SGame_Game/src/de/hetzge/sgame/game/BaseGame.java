package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.entity.EntityFactory;
import de.hetzge.sgame.entity.EntityModule;
import de.hetzge.sgame.entity.EntityOnMapService;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.ki.EntityKI;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.map.MapModule;
import de.hetzge.sgame.message.MessageModule;
import de.hetzge.sgame.network.NetworkConfig;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.sync.SyncModule;

public class BaseGame extends Application {

	protected final MapModule mapModule;
	protected final NetworkModule networkModule;
	protected final SyncModule syncModule;
	protected final EntityModule entityModule;
	protected final MessageModule messageModule;
	protected final NetworkConfig networkConfig;
	protected final EntityFactory entityFactory;
	protected final EntityPool entityPool;
	protected final EntityOnMapService onMapService;

	public BaseGame(Class<? extends BootstrapperBundle> bootstrapperBundle) {
		super(bootstrapperBundle);
		this.mapModule = this.get(MapModule.class);
		this.networkModule = this.get(NetworkModule.class);
		this.networkConfig = this.get(NetworkConfig.class);
		this.syncModule = this.get(SyncModule.class);
		this.entityModule = this.get(EntityModule.class);
		this.entityFactory = this.get(EntityFactory.class);
		this.entityPool = this.get(EntityPool.class);
		this.messageModule = this.get(MessageModule.class);
		this.onMapService = this.get(EntityOnMapService.class);

		this.modulePool.registerModules(this.mapModule, this.networkModule, this.syncModule, this.entityModule, this.messageModule);

		this.entityFactory.registerFactory(EntityType.SILLY_BLOCK, (entity) -> {
			entity.setEntityKey(EntityType.SILLY_BLOCK);
			entity.setOrientation(Orientation.SOUTH);
			entity.setRealDimension(new XY(10f, 10f));
			entity.setDimension(new XY(40f, 40f));
			entity.setCollision(this.onMapService.on(entity.getRealRectangle()).asCollisionArray());
			entity.setEntityKI(new EntityKI(entity));

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

	}

}
