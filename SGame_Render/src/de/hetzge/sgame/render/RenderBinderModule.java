package de.hetzge.sgame.render;

import se.jbee.inject.bind.BinderModule;

public class RenderBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(RenderConfig.class).to(RenderConfig.class);
		this.bind(RenderableRessourcePool.class).to(RenderableRessourcePool.class);
		this.bind(RenderableIdPool.class).to(RenderableIdPool.class);
		this.bind(RenderPool.class).to(RenderPool.class);
		this.bind(RenderBinderModule.class).to(RenderBinderModule.class);
		this.bind(RenderService.class).to(RenderService.class);
		this.bind(RenderModule.class).to(RenderModule.class);
	}

}
