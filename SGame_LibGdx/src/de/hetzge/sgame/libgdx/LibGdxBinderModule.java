package de.hetzge.sgame.libgdx;

import se.jbee.inject.bind.BinderModule;
import de.hetzge.sgame.render.IF_RenderableFactory;
import de.hetzge.sgame.render.IF_RenderableLoader;

public class LibGdxBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(LibGdxApplication.class).to(LibGdxApplication.class);
		this.bind(LibGdxConfig.class).to(LibGdxConfig.class);
		this.bind(LibGdxModule.class).to(LibGdxModule.class);
		this.bind(IF_RenderableFactory.class).to(LibGdxRenderableFactory.class);
		this.bind(IF_RenderableLoader.class).to(LibGdxRenderableLoader.class);
	}

}
