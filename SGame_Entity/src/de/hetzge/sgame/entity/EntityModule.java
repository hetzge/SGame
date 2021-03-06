package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.common.IF_MapProvider;
import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.entity.message.AddEntitiesMessage;
import de.hetzge.sgame.entity.message.AddEntitiesMessageHandler;
import de.hetzge.sgame.entity.message.NewEntityMessage;
import de.hetzge.sgame.entity.message.NewEntityMessageHandler;
import de.hetzge.sgame.entity.message.RemoveEntityMessage;
import de.hetzge.sgame.entity.message.RemoveEntityMessageHandler;
import de.hetzge.sgame.message.MessageConfig;
import de.hetzge.sgame.message.MessageHandlerPool;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;

public class EntityModule implements IF_Module, IF_Renderable<IF_RenderableContext> {

	private final EntityOnMapThread entityOnMapThread;

	public final EntityPool entityPool;
	public final EntityFactory entityFactory;
	public final EntityConfig entityConfig;
	public final ActiveEntityMap activeEntityMap;
	public final IF_MapProvider mapProvider;
	public final MessageHandlerPool messageHandlerPool;
	public final MessageConfig messageConfig;
	private final EntityRenderer entityRenderer;
	private final FSTConfiguration fstConfiguration;

	public EntityModule(EntityOnMapThread entityOnMapThread, EntityPool entityPool, EntityRenderer entityRenderer, EntityFactory entityFactory, EntityConfig entityConfig, ActiveEntityMap activeEntityMap, IF_MapProvider mapProvider, MessageHandlerPool messageHandlerPool, MessageConfig messageConfig, FSTConfiguration fstConfiguration) {
		this.entityOnMapThread = entityOnMapThread;
		this.entityPool = entityPool;
		this.entityRenderer = entityRenderer;
		this.entityFactory = entityFactory;
		this.entityConfig = entityConfig;
		this.activeEntityMap = activeEntityMap;
		this.mapProvider = mapProvider;
		this.messageHandlerPool = messageHandlerPool;
		this.messageConfig = messageConfig;
		this.fstConfiguration = fstConfiguration;
	}

	@Override
	public void init() {
		this.messageHandlerPool.registerMessageHandler(NewEntityMessage.class, this.get(NewEntityMessageHandler.class));
		this.messageHandlerPool.registerMessageHandler(RemoveEntityMessage.class, this.get(RemoveEntityMessageHandler.class));
		this.messageHandlerPool.registerMessageHandler(AddEntitiesMessage.class, this.get(AddEntitiesMessageHandler.class));
		this.messageConfig.serverToNewClientMessages.add((Supplier<?>) () -> {
			return new AddEntitiesMessage(new ArrayList<>(this.entityPool.getEntities()));
		});

		this.entityPool.init();
	}

	@Override
	public void postInit() {
		this.entityOnMapThread.start();
	}

	@Override
	public void update() {
		this.entityPool.update();
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.entityRenderer.render(context);
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {
		this.entityRenderer.renderShapes(context);
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
		this.entityRenderer.renderFilledShapes(context);
	}

}
