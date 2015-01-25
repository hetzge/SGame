package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.IF_DependencyInjection;

public interface IF_Module extends IF_DependencyInjection {

	public void init();

	public void postInit();

	public void update();

}
