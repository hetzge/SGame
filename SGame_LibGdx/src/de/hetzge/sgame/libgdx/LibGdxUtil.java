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
		Texture texture = new Texture(Gdx.files.internal(path));
		TextureRegion[][] textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

		int widthInFrames = endFrameX - startFrameX + 1;
		int heightInFrames = endFrameY - startFrameY + 1;

		TextureRegion[] animationFrames = new TextureRegion[widthInFrames * heightInFrames];
		for (int x = 0; x < widthInFrames; x++) {
			for (int y = 0; y < heightInFrames; y++) {
				TextureRegion textureRegion = textureRegions[startFrameY + y - 1][startFrameX + x - 1];
				textureRegion.flip(false, true);
				animationFrames[y * (widthInFrames - 1) + x] = textureRegion;
			}
		}
		return new Animation(0.5f, animationFrames);
	}

}
