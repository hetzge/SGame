package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.hetzge.sgame.common.definition.IF_Module;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableLine;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableRectangle;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;

public class LibGdxModule implements IF_Module {

	private final RenderConfig renderConfig;

	public LibGdxModule(RenderConfig renderConfig) {
		this.renderConfig = renderConfig;
	}

	@Override
	public void init() {
		// TODO add other shapes
		this.renderConfig.initRenderableConsumers.add((renderPool) -> {
			renderPool.registerRenderableRessource(PredefinedRenderId.RECTANGLE, new LibGdxRenderableRectangle());
			renderPool.registerRenderableRessource(PredefinedRenderId.LINE, new LibGdxRenderableLine());
		});
	}

	@Override
	public void postInit() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = LibGdxConfig.INSTANCE.gameTitle;
		cfg.useGL30 = false;
		cfg.allowSoftwareMode = false;
		cfg.vSyncEnabled = false;
		cfg.resizable = true;
		cfg.width = LibGdxConfig.INSTANCE.screenWidth;
		cfg.height = LibGdxConfig.INSTANCE.screenHeight;
		new LwjglApplication(this.get(LibGdxApplication.class), cfg);
	}

	@Override
	public void update() {

	}

}
