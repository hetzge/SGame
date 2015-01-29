package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.Set;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutableComplexRectangle;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.common.timer.Timer;
import de.hetzge.sgame.entity.message.AddEntitiesMessage;
import de.hetzge.sgame.entity.message.AddEntitiesMessageHandler;
import de.hetzge.sgame.entity.message.NewEntityMessage;
import de.hetzge.sgame.entity.message.NewEntityMessageHandler;
import de.hetzge.sgame.entity.message.RemoveEntityMessage;
import de.hetzge.sgame.entity.message.RemoveEntityMessageHandler;
import de.hetzge.sgame.entity.module.CollisionModule;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;

public class EntityModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	private class CollisionThread extends Thread {

		public CollisionThread() {
			super("collision_thread");
		}

		@Override
		public void run() {
			Timer updateEntityOnMapTimer = new Timer(1000);
			Timer updateEntityCollision = new Timer(1000);

			while (true) {
				Util.sleep(100);
				if (updateEntityOnMapTimer.isTime()) {
					Set<PositionAndDimensionModule> positionAndDimensionModules = EntityModule.this.entityPool.getEntityModulesByModuleClass(PositionAndDimensionModule.class);
					for (PositionAndDimensionModule positionAndDimensionModule : positionAndDimensionModules) {
						this.updateEntityOnMap(positionAndDimensionModule);
					}
				}
				if (updateEntityCollision.isTime()) {
					Set<CollisionModule> collisionModules = EntityModule.this.entityPool.getEntityModulesByModuleClass(CollisionModule.class);
					for (CollisionModule collisionModule : collisionModules) {
						this.updateCollisionOnMap(collisionModule);
					}
				}
			}
		}

		private void updateCollisionOnMap(CollisionModule collisionModule) {
			if (collisionModule.entity.positionAndDimensionModuleCache.isAvailable()) {
				PositionAndDimensionModule module = collisionModule.entity.positionAndDimensionModuleCache.get();
				IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> positionAndDimensionRectangle = module.getPositionAndDimensionRectangle();

				int startCollisionTileX = Math.round(positionAndDimensionRectangle.getStartPosition().getX() / EntityModule.this.mapProvider.provide().getCollisionTileSize());
				int startCollisionTileY = Math.round(positionAndDimensionRectangle.getStartPosition().getY() / EntityModule.this.mapProvider.provide().getCollisionTileSize());

				if (module.isFixed()) {
					EntityModule.this.mapProvider.provide().getFixEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, collisionModule.getActiveCollisionMap());
				} else {
					EntityModule.this.mapProvider.provide().getFlexibleEntityCollisionMap().connect(startCollisionTileX, startCollisionTileY, collisionModule.getActiveCollisionMap());
				}
			}
		}

		private void updateEntityOnMap(PositionAndDimensionModule positionAndDimensionModule) {
			IF_ImmutableComplexRectangle<InterpolatePosition, Dimension> rectangle = positionAndDimensionModule.getPositionAndDimensionRectangle();
			int x = EntityModule.this.mapProvider.provide().convertPxInTile(rectangle.getX());
			int y = EntityModule.this.mapProvider.provide().convertPxInTile(rectangle.getY());
			EntityModule.this.activeEntityMap.connect(x, y, positionAndDimensionModule.getEntityOnMap());
		}
	}

	private final CollisionThread collisionThread;

	public final EntityPool entityPool;
	public final EntityFactory entityFactory;
	public final EntityConfig entityConfig;
	public final ActiveEntityMap activeEntityMap;
	public final IF_MapProvider mapProvider;
	public final MessageHandlerPool messageHandlerPool;
	public final MessageConfig messageConfig;

	public EntityModule(EntityPool entityPool, EntityFactory entityFactory, EntityConfig entityConfig, ActiveEntityMap activeEntityMap, IF_MapProvider mapProvider, MessageHandlerPool messageHandlerPool, MessageConfig messageConfig) {
		this.collisionThread = new CollisionThread();
		this.entityPool = entityPool;
		this.entityFactory = entityFactory;
		this.entityConfig = entityConfig;
		this.activeEntityMap = activeEntityMap;
		this.mapProvider = mapProvider;
		this.messageHandlerPool = messageHandlerPool;
		this.messageConfig = messageConfig;
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(NewEntityMessage.class, this.get(NewEntityMessageHandler.class));
		this.messageHandlerPool.registerMessageHandler(RemoveEntityMessage.class, this.get(RemoveEntityMessageHandler.class));
		this.messageHandlerPool.registerMessageHandler(AddEntitiesMessage.class, this.get(AddEntitiesMessageHandler.class));

		this.messageConfig.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new AddEntitiesMessage(new ArrayList<>(this.entityPool.getEntities()));
		});

		this.entityPool.init();
	}

	@Override
	public void postInit() {
		this.collisionThread.start();
	}

	@Override
	public void update() {
		this.entityPool.update();
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.entityPool.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

}
