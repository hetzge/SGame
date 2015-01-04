package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.common.geometry.Rectangle;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableTextureRegion implements IF_RenderableWrapper<LibGdxRenderableContext> {

	private final TextureRegion textureRegion;

	public LibGdxRenderableTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		LibGdxRenderableTextureRegion.render(this.textureRegion, context, onScreen);
	}

	public static void render(TextureRegion textureRegion, LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		Rectangle rectangle = onScreen.getRenderedRectangle();
		context.spriteBatch.draw(textureRegion, rectangle.getStartPosition().getX(), rectangle.getStartPosition().getY(), rectangle.getDimension().getWidth(), rectangle.getDimension().getHeight());
	}

	@Override
	public TextureRegion getNativeObject() {
		return this.textureRegion;
	}

}
