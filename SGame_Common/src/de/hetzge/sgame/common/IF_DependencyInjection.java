package de.hetzge.sgame.common;

import se.jbee.inject.DIRuntimeException.NoSuchResourceException;
import se.jbee.inject.Dependency;
import de.hetzge.sgame.common.application.Application;

public interface IF_DependencyInjection {

	public default <T> T get(Class<T> clazz) {
		return Application.INJECTOR.resolve(Dependency.dependency(clazz));
	}

	public default <T> T getOrNull(Class<T> clazz){
		try{
			return this.get(clazz);
		} catch (NoSuchResourceException  ex){
			return null;
		}
	}

}
