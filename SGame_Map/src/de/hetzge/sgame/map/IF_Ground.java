package de.hetzge.sgame.map;

import java.io.Serializable;

import de.hetzge.sgame.render.IF_PixelAccess;

public interface IF_Ground extends Serializable {

	public GroundType getGroundType();

	public IF_PixelAccess getTemplatePixelAccess();

}
