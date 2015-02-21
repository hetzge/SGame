package de.hetzge.sgame.entity;

import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;
import de.hetzge.sgame.entity.ki.high.CollectorKI;
import de.hetzge.sgame.entity.ki.high.SillyWalkerKI;
import de.hetzge.sgame.entity.ki.low.GotoKI;
import de.hetzge.sgame.entity.message.AddEntitiesMessageHandler;
import de.hetzge.sgame.entity.message.NewEntityMessageHandler;
import de.hetzge.sgame.entity.message.RemoveEntityMessageHandler;

public class EntityBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(EntityModule.class).to(EntityModule.class);
		this.bind(EntityConfig.class).to(EntityConfig.class);
		this.bind(EntityPool.class).to(EntityPool.class);
		this.bind(EntityFactory.class).to(EntityFactory.class);
		this.bind(NewEntityMessageHandler.class).to(NewEntityMessageHandler.class);
		this.bind(RemoveEntityMessageHandler.class).to(RemoveEntityMessageHandler.class);
		this.bind(AddEntitiesMessageHandler.class).to(AddEntitiesMessageHandler.class);
		this.bind(ActiveEntityMap.class).to(ActiveEntityMap.class);
		this.bind(EntityOnMapThread.class).to(EntityOnMapThread.class);
		this.bind(EntityOnMapService.class).to(EntityOnMapService.class);

		this.per(Scoped.INJECTION).bind(SillyWalkerKI.class).to(SillyWalkerKI.class);
		this.per(Scoped.INJECTION).bind(CollectorKI.class).to(CollectorKI.class);
		this.per(Scoped.INJECTION).bind(GotoKI.class).to(GotoKI.class);
	}
}
