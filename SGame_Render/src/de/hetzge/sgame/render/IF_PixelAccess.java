package de.hetzge.sgame.render;

import java.awt.Color;

public interface IF_PixelAccess {

	public Color getColor(int x, int y);

	public int getWidth();

	public int getHeight();

}
