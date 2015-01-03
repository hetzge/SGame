package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.graphics.Color;

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

}
