package de.hetzge.sgame.libgdx;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTextureRegion;
import de.hetzge.sgame.render.IF_RenderableLoader;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class LibGdxRenderableLoader implements IF_RenderableLoader {

	// TODO use data objects and special objs (Tileset ...)

	@Override
	public int[] loadTilesets(String[] paths, float[] widths, float[] heights, int tileSize) {
		List<Integer> resultRenderIds = new LinkedList<Integer>();
		for (String path : paths) {

			int widthInTiles = (int) Math.floor(texture.getWidth() / tileSize);
			int heightInTiles = (int) Math.floor(texture.getHeight() / tileSize);
			int[] renderIds = new int[widthInTiles * heightInTiles];

			for (int i = 0; i < renderIds.length; i++) {
				renderIds[i] = RenderUtil.getNextRenderId();
			}

			RenderConfig.INSTANCE.initRenderableConsumers.add((renderableRessourcePool) -> {
				Texture texture = new Texture(Gdx.files.internal(path));
				for (int x = 0; x < widthInTiles; x++) {
					for (int y = 0; y < heightInTiles; y++) {
						int renderId = renderIds[y * widthInTiles + x];
						renderableRessourcePool.registerRenderableRessource(renderId, new LibGdxRenderableTextureRegion(new TextureRegion(texture, x * tileSize, y * tileSize, tileSize, tileSize)));
						resultRenderIds.add(renderId);
					}
				}
			});
		}

		int[] result = new int[resultRenderIds.size()];
		int i = 0;
		for (Integer renderId : resultRenderIds) {
			result[i++] = renderId;
		}

		return result;
	}
}
