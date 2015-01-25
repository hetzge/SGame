package de.hetzge.sgame.game;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.application.Application;
import de.hetzge.sgame.common.definition.IF_EntityType;
import de.hetzge.sgame.map.GroundType;
import de.hetzge.sgame.map.IF_Ground;
import de.hetzge.sgame.render.IF_AnimationKey;
import de.hetzge.sgame.render.IF_PixelAccess;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.RenderableRessourcePool;

public class Definition {

	public static enum EntityType implements IF_EntityType {
		SILLY_BLOCK, TREE;
	}

	public static enum AnimationKey implements IF_AnimationKey {
		IDLE, WALK, WORK, FIGHT;
	}

	public final static class RenderId {
		public static final int GRASS_RENDERABLE_ID = RenderService.getNextRenderId();
		public static final int DESERT_RENDERABLE_ID = RenderService.getNextRenderId();
		public static final int HERO_SPRITE_RENDERABLE_ID = RenderService.getNextRenderId();

		private RenderId() {
		}
	}

	public static enum Ground implements IF_Ground {

		GRASS(GroundType.SMOOTH, RenderId.GRASS_RENDERABLE_ID), WATER(GroundType.SMOOTH, RenderId.DESERT_RENDERABLE_ID);

		public final GroundType groundType;
		public final int renderableKey;

		private Ground(GroundType groundType, int renderableKey) {
			this.groundType = groundType;
			this.renderableKey = renderableKey;
		}

		@Override
		public GroundType getGroundType() {
			return this.groundType;
		}

		@Override
		public IF_PixelAccess getTemplatePixelAccess() {
			return (IF_PixelAccess) Application.INJECTOR.resolve(Dependency.dependency(RenderableRessourcePool.class)).getRenderableRessource(this.renderableKey);
		}

	}

}
