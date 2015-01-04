package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableTexture implements IF_RenderableWrapper<LibGdxRenderableContext> {

	private final Texture texture;

	public LibGdxRenderableTexture(String path) {
		this(new Texture(Gdx.files.internal(path)));
	}

	public LibGdxRenderableTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		Rectangle rectangle = onScreen.getRenderedRectangle();
		context.spriteBatch.draw(this.texture, rectangle.getStartPosition().getX(), rectangle.getStartPosition().getY(), rectangle.getDimension().getWidth(), rectangle.getDimension().getHeight());
	}

	@Override
	public Texture getNativeObject() {
		return this.texture;
	}

}
