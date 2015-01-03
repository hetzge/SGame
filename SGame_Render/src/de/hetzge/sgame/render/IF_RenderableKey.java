package de.hetzge.sgame.render;

public interface IF_RenderableKey {
	public static final IF_RenderableKey DEFAULT_RENDERABLE_KEY = new DynamicRenderableKey("");

	public static final IF_RenderableKey DEFAULT_RECTANGLE_KEY = new DynamicRenderableKey("RECTANGLE");
	public static final IF_RenderableKey DEFAULT_LINE_KEY = new DynamicRenderableKey("LINE");
	public static final IF_RenderableKey DEFAULT_CYCLE_KEY = new DynamicRenderableKey("CYCLE");

	public static final IF_RenderableKey DEFAULT_FILLED_RECTANGLE_KEY = new DynamicRenderableKey("FILLED_RECTANGLE");
	public static final IF_RenderableKey DEFAULT_FILLED_LINE_KEY = new DynamicRenderableKey("FILLED_LINE");
	public static final IF_RenderableKey DEFAULT_FILLED_CYCLE_KEY = new DynamicRenderableKey("FILLED_CYCLE");
}
