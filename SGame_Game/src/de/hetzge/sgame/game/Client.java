package de.hetzge.sgame.game;

import java.util.Set;

import de.hetzge.sgame.application.ApplicationConfig;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;
import de.hetzge.sgame.common.geometry.InterpolatePosition;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityConfig;
import de.hetzge.sgame.entity.module.PositionAndDimensionModule;
import de.hetzge.sgame.game.Definition.AnimationKey;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.libgdx.LibGdxModule;
import de.hetzge.sgame.libgdx.PixmapWrapper;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableAnimation;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTexture;
import de.hetzge.sgame.map.MapConfig;
import de.hetzge.sgame.network.NetworkConfig;
import de.hetzge.sgame.network.PeerRole;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderModule;
import de.hetzge.sgame.render.RenderableKey;

public class Client extends BaseGame {

	private final LibGdxModule libGdxModule;
	private final RenderModule renderModule;

	public Client() {
		super();
		NetworkConfig.INSTANCE.peerRole = PeerRole.CLIENT;
		RenderConfig.INSTANCE.renderPool.registerComponentToRender(this.mapModule);
		RenderConfig.INSTANCE.renderPool.registerComponentToRender(this.entityModule);

		this.libGdxModule = new LibGdxModule();
		this.renderModule = new RenderModule();

		ApplicationConfig.INSTANCE.modulePool.registerModules(this.libGdxModule, this.renderModule);

		RenderConfig.INSTANCE.initRenderableConsumers.add((renderablePool) -> {
			renderablePool.registerRenderableRessource(PredefinedRenderId.DEFAULT, new PixmapWrapper("assets/ground/grass.png"));
			renderablePool.registerRenderableRessource(new RenderableKey().animationKey(AnimationKey.IDLE), new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 1, 3, 1));
			renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(AnimationKey.WALK).orientation(Orientation.NORTH), new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 1, 3, 1));
			renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(AnimationKey.WALK).orientation(Orientation.EAST), new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 3, 3, 3));
			renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(AnimationKey.WALK).orientation(Orientation.SOUTH), new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 4, 3, 4));
			renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(AnimationKey.WALK).orientation(Orientation.WEST), new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 2, 3, 2));
			renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.TREE), new LibGdxRenderableTexture("assets/tree.png"));
			renderablePool.registerRenderableRessource(RenderId.GRASS_RENDERABLE_ID, new PixmapWrapper("assets/ground/grass.png"));
			renderablePool.registerRenderableRessource(RenderId.DESERT_RENDERABLE_ID, new PixmapWrapper("assets/ground/desert.png"));
			renderablePool.registerRenderableRessource(RenderId.HERO_SPRITE_RENDERABLE_ID, new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 1, 3, 1));
		});

		MapConfig.INSTANCE.tilePool.map(0, RenderId.GRASS_RENDERABLE_ID);
	}

	@Override
	public void update() {
		super.update();

		Set<Entity> entitiesByType = EntityConfig.INSTANCE.entityPool.getEntitiesByType(EntityType.SILLY_BLOCK);
		for (Entity entity : entitiesByType) {
			PositionAndDimensionModule module = entity.getModule(PositionAndDimensionModule.class);
			IF_ImmutablePosition<InterpolatePosition> position = module.getPositionAndDimensionRectangle().getPosition();
			// System.out.println(position.getStartValue() + " " +
			// position.getStartTime() + "  <" + position.getX() + " | " +
			// position.getY() + "> " + position.getEndTime() + " "
			// + position.getEndValue());
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

}
