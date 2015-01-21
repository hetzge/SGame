package de.hetzge.sgame.render;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.hetzge.sgame.common.definition.IF_Tileset;

public class RenderConfig {

	public static final RenderConfig INSTANCE = new RenderConfig();

	public final RenderableRessourcePool renderableRessourcePool = new RenderableRessourcePool();

	public final RenderableIdPool renderableIdPool = new RenderableIdPool();

	public final RenderPool renderPool = new RenderPool();

	public final Viewport viewport = new Viewport();

	public final List<Consumer<RenderableRessourcePool>> initRenderableConsumers = new LinkedList<>();

	public IF_RenderableLoader renderableLoader = new IF_RenderableLoader() {
		// TODO
		@Override
		public int[] loadTilesets(List<? extends IF_Tileset> tilesets) {
			return new int[1000];
		}
	};

	private RenderConfig() {
	}

}
