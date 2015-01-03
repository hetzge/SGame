package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hetzge.sgame.render.IF_RenderableContext;

public class LibGdxRenderableContext implements IF_RenderableContext {

	public final SpriteBatch spriteBatch;
	public final ShapeRenderer shapeRenderer;
	public final ShapeRenderer filledShapeRenderer;

	public LibGdxRenderableContext(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, ShapeRenderer filledShapeRenderer) {
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;
		this.filledShapeRenderer = filledShapeRenderer;
	}

}
