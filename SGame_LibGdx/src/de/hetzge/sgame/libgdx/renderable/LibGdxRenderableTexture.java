package de.hetzge.sgame.libgdx.renderable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LibGdxRenderableTexture extends LibGdxRenderableTextureRegion {

	public LibGdxRenderableTexture(String path) {
		this(new Texture(Gdx.files.internal(path)));
	}

	public LibGdxRenderableTexture(Texture texture) {
		super(new TextureRegion(texture, texture.getWidth(), texture.getHeight()));
	}

}
