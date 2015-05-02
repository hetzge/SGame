package de.hetzge.sgame.game;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.game.Definition.EntityType;
import de.hetzge.sgame.libgdx.LibGdxModule;
import de.hetzge.sgame.libgdx.PixmapWrapper;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableAnimation;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTexture;
import de.hetzge.sgame.map.TilePool;
import de.hetzge.sgame.render.DefaultAnimationKey;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderModule;
import de.hetzge.sgame.render.RenderPool;
import de.hetzge.sgame.render.RenderableKey;

public class Client extends BaseGame {

	private final LibGdxModule libGdxModule;
	private final RenderModule renderModule;
	private final RenderConfig renderConfig;
	private final RenderPool renderPool;
	private final TilePool tilePool;

	public Client(boolean singleplayer, boolean client, boolean server, Class<? extends BaseGameBootstrapperBundle> bootstrapperBundle) {
		super(singleplayer, client, server, bootstrapperBundle != null ? bootstrapperBundle : ClientBootstrapperBundle.class);

		Stopwatch stopwatch = new Stopwatch("Init Client");

		if (singleplayer || client) {
			this.syncConfig.setSyncThreadEnabled(true); // TODO

			this.renderConfig = this.get(RenderConfig.class);
			this.renderPool = this.get(RenderPool.class);
			this.renderPool.registerComponentToRender(this.mapModule);
			this.renderPool.registerComponentToRender(this.entityModule);

			this.libGdxModule = this.get(LibGdxModule.class);
			this.renderModule = this.get(RenderModule.class);
			this.tilePool = this.get(TilePool.class);


			this.modulePool.registerModules(this.libGdxModule, this.renderModule);

			this.renderConfig.initRenderableConsumers.add((renderablePool) -> {
				renderablePool.registerRenderableRessource(PredefinedRenderId.DEFAULT, new PixmapWrapper("assets/ground/grass.png"));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.IDLE).orientation(Orientation.SOUTH), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 1, 1, 1, 1));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.IDLE).orientation(Orientation.EAST), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 1, 1, 1, 1));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.IDLE).orientation(Orientation.NORTH), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 1, 1, 1, 1));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.IDLE).orientation(Orientation.WEST), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 1, 1, 1, 1));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.WALK).orientation(Orientation.SOUTH), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 1, 1, 1, 3));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.WALK).orientation(Orientation.EAST), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 3, 1, 3, 3, true));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.WALK).orientation(Orientation.NORTH), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 2, 1, 2, 3));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.SILLY_BLOCK).animationKey(DefaultAnimationKey.WALK).orientation(Orientation.WEST), new LibGdxRenderableAnimation("assets/sprite2.png", 40, 40, 3, 1, 3, 3));
				renderablePool.registerRenderableRessource(new RenderableKey().entityType(EntityType.TREE), new LibGdxRenderableTexture("assets/tree.png"));
				renderablePool.registerRenderableRessource(RenderId.GRASS_RENDERABLE_ID, new PixmapWrapper("assets/ground/grass.png"));
				renderablePool.registerRenderableRessource(RenderId.DESERT_RENDERABLE_ID, new PixmapWrapper("assets/ground/desert.png"));
				renderablePool.registerRenderableRessource(RenderId.HERO_SPRITE_RENDERABLE_ID, new LibGdxRenderableAnimation("assets/sprite.png", 32, 48, 1, 1, 3, 1));
			});

			this.tilePool.map(0, RenderId.GRASS_RENDERABLE_ID);

		} else {
			this.libGdxModule = null;
			this.renderModule = null;
			this.renderConfig = null;
			this.renderPool = null;
			this.tilePool = null;
		}

		stopwatch.stop();
	}

	@Override
	public void update() {
		//		if (this.server) {
		super.update();
		//		} 
	}

	public static void main(String[] args) {
		Client client = new Client(false, true, false, null);
		client.start();
	}

}
