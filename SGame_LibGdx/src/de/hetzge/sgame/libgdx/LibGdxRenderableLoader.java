package de.hetzge.sgame.libgdx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.common.definition.IF_Tileset;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTextureRegion;
import de.hetzge.sgame.render.IF_RenderableLoader;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderService;

public class LibGdxRenderableLoader implements IF_RenderableLoader {

	private final RenderConfig renderConfig;

	public LibGdxRenderableLoader(RenderConfig renderConfig) {
		this.renderConfig = renderConfig;
	}

	@Override
	public int[] loadTilesets(List<? extends IF_Tileset> tilesets) {

		List<Integer> resultRenderIds = new LinkedList<Integer>();
		for (IF_Tileset tileset : tilesets) {

			int widthInTiles = (int) Math.floor((float) tileset.getImageWidth() / tileset.getTileWidth());
			int heightInTiles = (int) Math.floor((float) tileset.getImageWidth() / tileset.getTileHeight());
			ArrayList<Integer> renderIds = new ArrayList<Integer>(widthInTiles * heightInTiles);

			for (int i = 0; i < widthInTiles * heightInTiles; i++) {
				renderIds.add(RenderService.getNextRenderId());
			}

			this.renderConfig.initRenderableConsumers.add((renderableRessourcePool) -> {
				Texture texture = new Texture(Gdx.files.internal(tileset.getImage()));
				for (int x = 0; x < widthInTiles; x++) {
					for (int y = 0; y < heightInTiles; y++) {
						int renderId = renderIds.get(y * widthInTiles + x);
						renderableRessourcePool.registerRenderableRessource(renderId, new LibGdxRenderableTextureRegion(new TextureRegion(texture, x * tileset.getTileWidth(), y * tileset.getTileHeight(), tileset.getTileWidth(), tileset.getTileHeight())));
						resultRenderIds.add(renderId);
					}
				}
			});

			resultRenderIds.addAll(renderIds);
		}

		int[] result = new int[resultRenderIds.size()];
		int i = 0;
		for (Integer renderId : resultRenderIds) {
			result[i++] = renderId;
		}

		return result;
	}
}
