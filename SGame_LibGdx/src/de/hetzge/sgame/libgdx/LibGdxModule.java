package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.render.RenderConfig;

public class LibGdxModule implements IF_Module {

	private LwjglApplication lwjglApplication;

	@Override
	public void init() {
		RenderConfig.INSTANCE.renderableFactory = new LibGdxRenderableFactory();
	}

	@Override
	public void update() {
		if (this.lwjglApplication == null) {
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = LibGdxConfig.INSTANCE.gameTitle;
			cfg.useGL30 = false;
			cfg.width = 1600;
			cfg.height = 1000;
			this.lwjglApplication = new LwjglApplication(new LibGdxApplication(), cfg);
		}
	}

}
