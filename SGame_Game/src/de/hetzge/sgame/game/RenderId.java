package de.hetzge.sgame.game;

import de.hetzge.sgame.render.RenderService;

public final class RenderId {

	public static final int GRASS_RENDERABLE_ID = RenderService.getNextRenderId();
	public static final int DESERT_RENDERABLE_ID = RenderService.getNextRenderId();
	public static final int HERO_SPRITE_RENDERABLE_ID = RenderService.getNextRenderId();

}
