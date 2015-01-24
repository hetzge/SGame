package de.hetzge.sgame.common;

import org.nustaq.serialization.FSTConfiguration;

public final class FstService {

	private final FSTConfiguration fstConfiguration;

	public FstService(FSTConfiguration fstConfiguration) {
		this.fstConfiguration = fstConfiguration;
	}

	public void registerClass(Class<?>... clazz) {
		this.fstConfiguration.registerClass(clazz);
	}

}
