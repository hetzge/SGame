package de.hetzge.sgame.render;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class RenderConfig {

	public final List<Consumer<RenderableRessourcePool>> initRenderableConsumers = new LinkedList<>();

	public RenderConfig() {
	}

}
