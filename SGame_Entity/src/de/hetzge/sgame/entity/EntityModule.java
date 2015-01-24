package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.Set;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
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
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;

public class EntityModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	private class CollisionThread extends Thread {

		public CollisionThread() {
			super("collision_thread");

		}

		@Override
		public void run() {
			EntityContext.INSTANCE.set(EntityModule.this);
			Timer updateEntityOnMapTimer = new Timer(1000);
			Timer updateEntityCollision = new Timer(1000);

			while (true) {
				Util.sleep(100);
				if (updateEntityOnMapTimer.isTime()) {
					Set<PositionAndDimensionModule> positionAndDimensionModules = EntityModule.this.entityPool.getEntityModulesByModuleClass(PositionAndDimensionModule.class);
					for (PositionAndDimensionModule positionAndDimensionModule : positionAndDimensionModules) {
						positionAndDimensionModule.updateEntityOnMap();
					}
				}
				if (updateEntityCollision.isTime()) {
					Set<CollisionModule> collisionModules = EntityModule.this.entityPool.getEntityModulesByModuleClass(CollisionModule.class);
					for (CollisionModule collisionModule : collisionModules) {
						collisionModule.updateCollisionOnMap();
					}
				}
			}
		}
	}

	private final CollisionThread collisionThread;

	public final EntityPool entityPool;
	public final EntityFactory entityFactory;
	public final EntityConfig entityConfig;
	public final ActiveEntityMap activeEntityMap;

	public EntityModule(EntityPool entityPool, EntityFactory entityFactory, EntityConfig entityConfig, ActiveEntityMap activeEntityMap) {
		this.collisionThread = new CollisionThread();
		this.entityPool = entityPool;
		this.entityFactory = entityFactory;
		this.entityConfig = entityConfig;
		this.activeEntityMap = activeEntityMap;
	}

	@Override
	public void init() {
		EntityContext.INSTANCE.set(EntityModule.this);
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(NewEntityMessage.class, this.get(NewEntityMessageHandler.class));
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(RemoveEntityMessage.class, this.get(RemoveEntityMessageHandler.class));
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(AddEntitiesMessage.class, this.get(AddEntitiesMessageHandler.class));

		MessageConfig.INSTANCE.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new AddEntitiesMessage(new ArrayList<>(this.entityPool.getEntities()));
		});

		this.entityPool.init();
	}

	@Override
	public void postInit() {
		EntityContext.INSTANCE.set(EntityModule.this);
		this.collisionThread.start();
	}

	@Override
	public void update() {
		EntityContext.INSTANCE.set(EntityModule.this);
		this.entityPool.update();
	}

	@Override
	public void render(IF_RenderableContext context) {
		EntityContext.INSTANCE.set(EntityModule.this);
		this.entityPool.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		EntityContext.INSTANCE.set(EntityModule.this);
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		EntityContext.INSTANCE.set(EntityModule.this);
	}

}
