package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.libgdx.LibGdxConfig;
import de.hetzge.sgame.render.IF_RenderInformation;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableAnimation implements IF_RenderableWrapper<LibGdxRenderableContext> {

	private final Animation animation;

	public LibGdxRenderableAnimation(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		TextureRegion keyFrame = this.animation.getKeyFrame(LibGdxConfig.INSTANCE.stateTime);
		LibGdxRenderableTextureRegion.render(keyFrame, context, onScreen);
	}

	@Override
	public Animation getNativeObject() {
		return this.animation;
	}
}