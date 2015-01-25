package de.hetzge.sgame.render;

public final class PredefinedRenderId {

	public static final int DEFAULT = 0;

	public static final int RECTANGLE = RenderService.getNextRenderId();
	public static final int CIRCLE = RenderService.getNextRenderId();
	public static final int LINE = RenderService.getNextRenderId();
	public static final int RECTANGLE_FILLED = RenderService.getNextRenderId();
	public static final int CIRCLE_FILLED = RenderService.getNextRenderId();

	private PredefinedRenderId() {
	}

}
