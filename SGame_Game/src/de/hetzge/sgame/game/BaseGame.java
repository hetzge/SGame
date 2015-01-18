package de.hetzge.sgame.game;

import de.hetzge.sgame.application.Application;
import de.hetzge.sgame.application.ApplicationConfig;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.entity.EntityModule;
import de.hetzge.sgame.entity.module.CollisionModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.entity.module.RenderableModule;
import de.hetzge.sgame.game.Client.AnimationKey;
import de.hetzge.sgame.map.GroundType;
import de.hetzge.sgame.map.IF_Ground;
import de.hetzge.sgame.map.MapModule;
import de.hetzge.sgame.message.MessageModule;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.render.IF_PixelAccess;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.sync.SyncModule;

public class BaseGame extends Application {

	public static enum EntityType implements IF_EntityType {
		SILLY_BLOCK;
	}

	public static enum Ground implements IF_Ground {

		GRASS(GroundType.SMOOTH, RenderId.GRASS_RENDERABLE_ID), WATER(GroundType.SMOOTH, RenderId.DESERT_RENDERABLE_ID);

		public final GroundType groundType;
		public final int renderableKey;

		private Ground(GroundType groundType, int renderableKey) {
			this.groundType = groundType;
			this.renderableKey = renderableKey;
		}

		@Override
		public GroundType getGroundType() {
			return this.groundType;
		}

		@Override
		public IF_PixelAccess getTemplatePixelAccess() {
			return (IF_PixelAccess) RenderConfig.INSTANCE.renderableRessourcePool.getRenderableRessource(this.renderableKey);
		}

	}

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
			renderableModule.setAnimationKey(AnimationKey.TEST);

			PositionAndDimensionModule positionAndDimensionModule = new PositionAndDimensionModule(entity);
			positionAndDimensionModule.setDimension(new Dimension(32f, 48f));

			CollisionModule collisionModule = new CollisionModule(entity);
			entity.registerModules(renderableModule, positionAndDimensionModule, collisionModule);
		});

	}
}
