package de.hetzge.sgame.game;

import de.hetzge.sgame.application.Application;
import de.hetzge.sgame.application.ApplicationConfig;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.entity.EntityModule;
import de.hetzge.sgame.entity.module.CollisionModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.entity.module.RenderableModule;
import de.hetzge.sgame.game.Definition.AnimationKey;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.map.MapModule;
import de.hetzge.sgame.message.MessageModule;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.sync.SyncModule;

public class BaseGame extends Application {

	protected final MapModule mapModule;
	protected final NetworkModule networkModule;
	protected final SyncModule syncModule;
	protected final EntityModule entityModule;
	protected final MessageModule messageModule;

	public BaseGame() {
		this.mapModule = new MapModule();
		this.networkModule = new NetworkModule();
		this.syncModule = new SyncModule();
		this.entityModule = new EntityModule();
		this.messageModule = new MessageModule();

		ApplicationConfig.INSTANCE.modulePool.registerModules(this.mapModule, this.networkModule, this.syncModule, this.entityModule, this.messageModule);

		EntityConfig.INSTANCE.entityFactory.registerFactory(EntityType.SILLY_BLOCK, (entity) -> {
			RenderableModule renderableModule = new RenderableModule(entity);

			renderableModule.setAnimationKey(AnimationKey.WALK);
			renderableModule.setEntityKey(EntityType.SILLY_BLOCK);
			renderableModule.setOrientation(Orientation.SOUTH);

			PositionAndDimensionModule positionAndDimensionModule = new PositionAndDimensionModule(entity);
			positionAndDimensionModule.setDimension(new Dimension(32f, 48f));

			CollisionModule collisionModule = new CollisionModule(entity);
			entity.registerModules(renderableModule, positionAndDimensionModule, collisionModule);
		});

		EntityConfig.INSTANCE.entityFactory.registerFactory(EntityType.TREE, (entity) -> {
			RenderableModule renderableModule = new RenderableModule(entity);
			renderableModule.setEntityKey(EntityType.TREE);

			PositionAndDimensionModule positionAndDimensionModule = new PositionAndDimensionModule(entity);
			positionAndDimensionModule.setDimension(new Dimension(64f, 64f));

			CollisionModule collisionModule = new CollisionModule(entity);
			collisionModule.setCollision(new boolean[][] { { false, true, true, true }, { false, true, true, true }, { false, true, true, true }, { false, true, true, true }, { false, true, true, true }, { false, true, true, true } });
			entity.registerModules(renderableModule, positionAndDimensionModule, collisionModule);
		});

	}
}
