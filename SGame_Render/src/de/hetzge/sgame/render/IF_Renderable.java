package de.hetzge.sgame.render;

public interface IF_Renderable<CONTEXT extends IF_RenderableContext> {

	public void render(CONTEXT context);

}
