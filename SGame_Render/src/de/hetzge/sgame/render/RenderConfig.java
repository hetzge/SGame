package de.hetzge.sgame.render;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.hetzge.sgame.common.geometry.ComplexRectangle;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.Position;

public class RenderConfig {

	public static final RenderConfig INSTANCE = new RenderConfig();

	public final RenderableRessourcePool renderableRessourcePool = new RenderableRessourcePool();

	public final RenderableIdPool renderableIdPool = new RenderableIdPool();

	public final RenderPool renderPool = new RenderPool();

	public final ComplexRectangle viewport = new ComplexRectangle(new Position(0f, 0f), new Dimension(0f, 0f));

	public final List<Consumer<RenderableRessourcePool>> initRenderableConsumers = new LinkedList<>();

	public IF_RenderableFactory renderableFactory;

	private RenderConfig() {
	}

}
