package de.hetzge.sgame.render;

import java.util.function.Consumer;

import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.common.geometry.Rectangle;

public class RenderConfig {

	public static final RenderConfig INSTANCE = new RenderConfig();

	/**
	 * Single render components like textures and other ressources
	 */
	public final RenderablePool renderablePool = new RenderablePool();

	public IF_RenderableFactory renderableFactory;

	public final Rectangle viewport = new Rectangle(new Position(0f, 0f), new Dimension(0f, 0f));

	public Consumer<RenderablePool> initRenderablePool = (renderablePool) -> {
	};

	private RenderConfig() {
	}

}
