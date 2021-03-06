package de.hetzge.sgame.libgdx;

import java.awt.Color;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableContext;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTexture;
import de.hetzge.sgame.render.IF_PixelAccess;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class PixmapWrapper implements IF_PixelAccess, IF_RenderableWrapper<LibGdxRenderableContext> {

	private final Pixmap pixmap;
	private LibGdxRenderableTexture libGdxRenderableTexture;

	public PixmapWrapper(String internalPathToFile) {
		this.pixmap = new Pixmap(Gdx.files.internal(internalPathToFile));
	}

	@Override
	public void render(LibGdxRenderableContext context, IF_RenderInformation onScreen) {
		if (this.libGdxRenderableTexture == null) {
			this.libGdxRenderableTexture = new LibGdxRenderableTexture(new Texture(this.pixmap));
		}
		this.libGdxRenderableTexture.render(context, onScreen);
	}

	@Override
	public Color getColor(int x, int y) {
		return new Color(this.pixmap.getPixel(x, y));
	}

	@Override
	public int getWidth() {
		return this.pixmap.getWidth();
	}

	@Override
	public int getHeight() {
		return this.pixmap.getHeight();
	}

	@Override
	public Pixmap getNativeObject() {
		return this.pixmap;
	}

}
