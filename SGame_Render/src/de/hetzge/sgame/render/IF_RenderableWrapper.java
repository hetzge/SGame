package de.hetzge.sgame.render;

public interface IF_RenderableWrapper<TYPE extends IF_RenderableContext> {
	public void render(TYPE context, IF_RenderInformation onScreen);

	public Object getNativeObject();
}
