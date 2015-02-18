package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.entity.EntityFactory;
import de.hetzge.sgame.entity.EntityModule;
import de.hetzge.sgame.entity.EntityPool;
import de.hetzge.sgame.entity.OnMapService;
import de.hetzge.sgame.entity.ki.EntityKI;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.entity.module.RenderableModule;
import de.hetzge.sgame.game.Definition.AnimationKey;
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
	protected final OnMapService onMapService;

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
		this.onMapService = this.get(OnMapService.class);

		this.modulePool.registerModules(this.mapModule, this.networkModule, this.syncModule, this.entityModule, this.messageModule);

		this.entityFactory.registerFactory(EntityType.SILLY_BLOCK, (entity) -> {
			RenderableModule renderableModule = new RenderableModule(entity);

			renderableModule.setAnimationKey(AnimationKey.WALK);
			renderableModule.setEntityKey(EntityType.SILLY_BLOCK);
			renderableModule.setOrientation(Orientation.SOUTH);

			PositionAndDimensionModule positionAndDimensionModule = new PositionAndDimensionModule(entity);
			positionAndDimensionModule.setDimension(new XY(32f, 48f));

			entity.registerModules(renderableModule, positionAndDimensionModule);

			entity.setEntityKI(new EntityKI(entity));
		});

		this.entityFactory.registerFactory(EntityType.TREE, (entity) -> {
			RenderableModule renderableModule = new RenderableModule(entity);
			renderableModule.setEntityKey(EntityType.TREE);

			PositionAndDimensionModule positionAndDimensionModule = new PositionAndDimensionModule(entity);
			positionAndDimensionModule.setDimension(new XY(64f, 64f));
			positionAndDimensionModule.setFixed(true);
			positionAndDimensionModule.setCollision(this.onMapService.on(positionAndDimensionModule).asCollisionArray());

			entity.registerModules(renderableModule, positionAndDimensionModule);
		});

	}

}
