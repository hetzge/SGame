package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class LibGdxUtil {

	private LibGdxUtil() {
	}

	public static Color convertAwtColor(java.awt.Color awtColor) {
		float r = awtColor.getRed() / 255f;
		float g = awtColor.getGreen() / 255f;
		float b = awtColor.getBlue() / 255f;
		float a = awtColor.getAlpha() / 255;
		return new Color(r, g, b, a);
	}

	public static Animation loadAnimation(String path, int frameWidth, int frameHeight, int startFrameX, int startFrameY, int endFrameX, int endFrameY) {
		return LibGdxUtil.loadAnimation(path, frameWidth, frameHeight, startFrameX, startFrameY, endFrameX, endFrameY, false);
	}

	public static Animation loadAnimation(String path, int frameWidth, int frameHeight, int startFrameX, int startFrameY, int endFrameX, int endFrameY, boolean flipHorizontal) {
		Texture texture = new Texture(Gdx.files.internal(path));
		TextureRegion[][] textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

		int widthInFrames = endFrameX - startFrameX + 1;
		int heightInFrames = endFrameY - startFrameY + 1;

		TextureRegion[] animationFrames = new TextureRegion[widthInFrames * heightInFrames];
		for (int x = 1; x <= widthInFrames; x++) {
			for (int y = 1; y <= heightInFrames; y++) {
				TextureRegion textureRegion = textureRegions[startFrameY + y - 2][startFrameX + x - 2];
				textureRegion.flip(flipHorizontal, true);
				animationFrames[(y - 1) * (widthInFrames ) + (x - 1)] = textureRegion;
			}
		}
		return new Animation(0.5f, animationFrames);
	}

}
