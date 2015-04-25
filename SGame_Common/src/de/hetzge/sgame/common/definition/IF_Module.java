package de.hetzge.sgame.common.definition;

import de.hetzge.sgame.common.IF_DependencyInjection;

public interface IF_Module extends IF_DependencyInjection {

	public void init();

	/**
	 * This step is called after on all modules the {@link IF_Module#init()}
	 * were called. This is useful if something depends on a other module and
	 * init order is not guaranted.
	 */
	public void postInit();

	public void update();

}
