package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.libgdx.LibGdxConfig;
import de.hetzge.sgame.libgdx.LibGdxUtil;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableAnimation implements IF_RenderableWrapper<LibGdxRenderableContext> {

	private final Animation animation;

	public LibGdxRenderableAnimation(String path, int frameWidth, int frameHeight, int startFrameX, int startFrameY, int endFrameX, int endFrameY) {
		this(path, frameWidth, frameHeight, startFrameX, startFrameY, endFrameX, endFrameY, false);
	}

	public LibGdxRenderableAnimation(String path, int frameWidth, int frameHeight, int startFrameX, int startFrameY, int endFrameX, int endFrameY, boolean flipHorizontal) {
		this(LibGdxUtil.loadAnimation(path, frameWidth, frameHeight, startFrameX, startFrameY, endFrameX, endFrameY, flipHorizontal));
	}

	public LibGdxRenderableAnimation(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		TextureRegion keyFrame = this.animation.getKeyFrame(LibGdxConfig.INSTANCE.stateTime, true);
		LibGdxRenderableTextureRegion.render(keyFrame, context, onScreen);
	}

	@Override
	public Animation getNativeObject() {
		return this.animation;
	}
}
