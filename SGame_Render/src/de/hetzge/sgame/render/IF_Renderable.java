package de.hetzge.sgame.render;

public interface IF_Renderable<CONTEXT extends IF_RenderableContext> {

	public void render(CONTEXT context);

	public void renderShapes(CONTEXT context);

	public void renderFilledShapes(CONTEXT context);

}
