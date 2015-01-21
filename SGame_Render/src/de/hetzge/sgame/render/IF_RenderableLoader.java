package de.hetzge.sgame.render;

import java.util.List;

import de.hetzge.sgame.common.definition.IF_Tileset;

public interface IF_RenderableLoader {

	public int[] loadTilesets(List<? extends IF_Tileset> tilesets);

}
