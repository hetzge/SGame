package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableRectangle;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class LibGdxModule implements IF_Module {

	private LwjglApplication lwjglApplication;

	@Override
	public void init() {
		RenderConfig.INSTANCE.renderableFactory = new LibGdxRenderableFactory();

		// TODO add other shapes
		RenderConfig.INSTANCE.initRenderableConsumers.add((renderPool) -> {
			renderPool.registerRenderableRessource(RenderUtil.getNextRenderId(), new LibGdxRenderableRectangle());
		});

	}

	@Override
	public void postInit() {
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
