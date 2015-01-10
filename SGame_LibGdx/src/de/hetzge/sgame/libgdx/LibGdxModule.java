package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableRectangle;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;

public class LibGdxModule implements IF_Module {

	@Override
	public void init() {
		RenderConfig.INSTANCE.renderableFactory = new LibGdxRenderableFactory();

		// TODO add other shapes
		RenderConfig.INSTANCE.initRenderableConsumers.add((renderPool) -> {
			renderPool.registerRenderableRessource(PredefinedRenderId.RECTANGLE, new LibGdxRenderableRectangle());
		});

	}

	@Override
	public void postInit() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = LibGdxConfig.INSTANCE.gameTitle;
		cfg.useGL30 = false;
		cfg.width = LibGdxConfig.INSTANCE.screenWidth;
		cfg.height = LibGdxConfig.INSTANCE.screenHeight;
		new LwjglApplication(new LibGdxApplication(), cfg);
	}

	@Override
	public void update() {

	}

}
