package de.hetzge.sgame.game;

import de.hetzge.sgame.libgdx.LibGdxBinderModule;
import de.hetzge.sgame.render.RenderBinderModule;

public class ClientBootstrapperBundle extends BaseGameBootstrapperBundle {

	@Override
	protected void bootstrap() {
		super.bootstrap();
		this.install(RenderBinderModule.class);
		this.install(LibGdxBinderModule.class);
	}

}
