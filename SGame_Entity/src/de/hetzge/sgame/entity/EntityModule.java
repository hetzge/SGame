package de.hetzge.sgame.entity;

import java.util.Set;

import de.hetzge.sgame.common.Util;
import de.hetzge.sgame.common.definition.IF_Callback;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.entity.message.AddEntitiesMessage;
import de.hetzge.sgame.entity.message.AddEntitiesMessageHandler;
import de.hetzge.sgame.entity.message.NewEntityMessage;
import de.hetzge.sgame.entity.message.NewEntityMessageHandler;
import de.hetzge.sgame.entity.message.RemoveEntityMessage;
import de.hetzge.sgame.entity.message.RemoveEntityMessageHandler;
import de.hetzge.sgame.entity.module.CollisionModule;
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
			while (true) {
				Util.sleep(100);
				Set<CollisionModule> collisionModules = EntityConfig.INSTANCE.entityPool.getEntityModulesByModuleClass(CollisionModule.class);
				for (CollisionModule collisionModule : collisionModules) {
					collisionModule.updateCollisionOnMap();
				}
			}
		}
	}

	private final CollisionThread collisionThread;

	public EntityModule() {
		this.collisionThread = new CollisionThread();
	}

	@Override
	public void init() {
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(NewEntityMessage.class, new NewEntityMessageHandler());
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(RemoveEntityMessage.class, new RemoveEntityMessageHandler());
		MessageConfig.INSTANCE.messageHandlerPool.registerMessageHandler(AddEntitiesMessage.class, new AddEntitiesMessageHandler());

		MessageConfig.INSTANCE.serverToNewClientMessages.add((IF_Callback<Object>) () -> {
			return new AddEntitiesMessage(EntityConfig.INSTANCE.entityPool.getEntitiesCopy());
		});

		EntityConfig.INSTANCE.entityPool.init();
	}

	@Override
	public void postInit() {
		this.collisionThread.start();
	}

	@Override
	public void update() {
		EntityConfig.INSTANCE.entityPool.update();
	}

	@Override
	public void render(IF_RenderableContext context) {
		EntityConfig.INSTANCE.entityPool.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

}
