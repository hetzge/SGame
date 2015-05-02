package de.hetzge.sgame.game;

import se.jbee.inject.bootstrap.BootstrapperBundle;
import de.hetzge.sgame.common.CommonBinderModule;
import de.hetzge.sgame.entity.EntityBinderModule;
import de.hetzge.sgame.entity.ki.KIBinderModule;
import de.hetzge.sgame.libgdx.LibGdxBinderModule;
import de.hetzge.sgame.map.MapBinderModule;
import de.hetzge.sgame.message.MessageBinderModule;
import de.hetzge.sgame.network.NetworkBinderModule;
import de.hetzge.sgame.render.RenderBinderModule;
import de.hetzge.sgame.sync.SyncBinderModule;

public class BaseGameBootstrapperBundle extends BootstrapperBundle {

	@Override
	protected void bootstrap() {
		this.install(EntityBinderModule.class);
		this.install(CommonBinderModule.class);
		this.install(MapBinderModule.class);
		this.install(SyncBinderModule.class);
		this.install(MessageBinderModule.class);
		this.install(NetworkBinderModule.class);
		this.install(RenderBinderModule.class);
		this.install(LibGdxBinderModule.class);
		this.install(KIBinderModule.class);
	}

}
