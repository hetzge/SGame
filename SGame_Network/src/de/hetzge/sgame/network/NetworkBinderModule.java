package de.hetzge.sgame.network;

import se.jbee.inject.bind.BinderModule;

public class NetworkBinderModule extends BinderModule {

	@Override
	protected void declare() {
		this.bind(Server.class).to(Server.class);
		this.bind(Client.class).to(Client.class);
		this.bind(NetworkConfig.class).to(NetworkConfig.class);
		this.bind(NetworkModule.class).to(NetworkModule.class);
	}

}
