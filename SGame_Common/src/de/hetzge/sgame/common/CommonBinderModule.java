package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

import se.jbee.inject.bind.BinderModule;

public class CommonBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(AStarService.class).to(AStarService.class);
		this.bind(FstService.class).to(FstService.class);
		this.bind(CommonConfig.class).to(CommonConfig.class);
		this.bind(ModulePool.class).to(ModulePool.class);
		this.bind(CommonConfig.class).to(CommonConfig.class);
		this.bind(FSTConfiguration.class).to((produced, injected) -> {
			return FSTConfiguration.getDefaultConfiguration();
		});
	}

}
