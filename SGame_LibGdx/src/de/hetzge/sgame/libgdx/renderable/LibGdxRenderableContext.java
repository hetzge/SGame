package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hetzge.sgame.render.IF_RenderableContext;

public class LibGdxRenderableContext implements IF_RenderableContext {

	public final SpriteBatch spriteBatch;

	public LibGdxRenderableContext(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
	}

}
