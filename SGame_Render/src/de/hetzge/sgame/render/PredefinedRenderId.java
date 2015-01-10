package de.hetzge.sgame.render;

public final class PredefinedRenderId {

	public static final int DEFAULT = 0;

	public static final int RECTANGLE = RenderUtil.getNextRenderId();
	public static final int CIRCLE = RenderUtil.getNextRenderId();
	public static final int LINE = RenderUtil.getNextRenderId();
	public static final int RECTANGLE_FILLED = RenderUtil.getNextRenderId();
	public static final int CIRCLE_FILLED = RenderUtil.getNextRenderId();

	private PredefinedRenderId() {
	}

}
