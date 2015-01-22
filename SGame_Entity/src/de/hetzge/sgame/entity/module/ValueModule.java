package de.hetzge.sgame.entity.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.hetzge.sgame.entity.BaseEntityModule;
import de.hetzge.sgame.entity.Entity;

public class ValueModule extends BaseEntityModule {

	private class Container implements Serializable {

		private class Value implements Serializable {
			int available;
			int max;
		}

		private final Map<Object, Integer> valueByKey = new HashMap<>();
		private final Map<Object, Integer> maxByKey = new HashMap<>();

		private boolean add(Object key, int amount) {

		}

	}

	public ValueModule(Entity entity) {
		super(entity);
	}

	@Override
	public void initImpl() {
	}

	@Override
	public void updateImpl() {
	}

}
