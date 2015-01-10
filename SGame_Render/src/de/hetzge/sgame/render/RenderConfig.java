package de.hetzge.sgame.render;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.hetzge.sgame.common.geometry.PrimitivRectangle;

public class RenderConfig {

	public static final RenderConfig INSTANCE = new RenderConfig();

	public final RenderableRessourcePool renderableRessourcePool = new RenderableRessourcePool();

	public final RenderableIdPool renderableIdPool = new RenderableIdPool();

	public final RenderPool renderPool = new RenderPool();

	public final PrimitivRectangle viewport = new PrimitivRectangle();

	public final List<Consumer<RenderableRessourcePool>> initRenderableConsumers = new LinkedList<>();

	public IF_RenderableFactory renderableFactory;

	private RenderConfig() {
	}

}
