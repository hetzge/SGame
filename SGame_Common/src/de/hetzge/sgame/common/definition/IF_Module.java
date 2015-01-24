package de.hetzge.sgame.common.definition;

import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.application.Application;

public interface IF_Module {

	public void init();

	public void postInit();

	public void update();

	public default <T> T get(Class<T> clazz) {
		return Application.INJECTOR.resolve(Dependency.dependency(clazz));
	}

}
