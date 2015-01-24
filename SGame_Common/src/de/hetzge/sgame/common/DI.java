package de.hetzge.sgame.common;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;

public class DI extends BootstrapperBundle {

	public static class RootModule extends BinderModule {
		@Override
		protected void declare() {
			this.bind(int.class).to(42);
		}
	}

	@Override
	protected void bootstrap() {
		this.install(RootModule.class);
	}

	public static void main(String[] args) {
		Injector injector = Bootstrap.injector(DI.class);

		int answer = injector.resolve(Dependency.dependency(int.class));
		System.out.println(answer);

	}
}
