package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableTextureRegion implements IF_RenderableWrapper<LibGdxRenderableContext> {

	private final TextureRegion textureRegion;

	public LibGdxRenderableTextureRegion(TextureRegion textureRegion) {
		textureRegion.flip(false, true);
		this.textureRegion = textureRegion;
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		LibGdxRenderableTextureRegion.render(this.textureRegion, context, onScreen);
	}

	public static void render(TextureRegion textureRegion, LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		if(context == null){
			throw new IllegalStateException("context is null");
		}

		if(textureRegion == null){
			throw new IllegalStateException("textureRegion is null");
		}

		if(onScreen == null){
			throw new IllegalStateException("onScreen is null");
		}


		IF_Rectangle_ImmutableView rectangle = onScreen.getRenderedRectangle();

		IF_Position_ImmutableView positionA = rectangle.getPositionA();
		IF_Dimension_ImmutableView dimension = rectangle.getDimension();

		context.spriteBatch.draw(textureRegion, positionA.getFX(), positionA.getFY(), dimension.getWidth(), dimension.getHeight());
	}

	@Override
	public TextureRegion getNativeObject() {
		return this.textureRegion;
	}

}
