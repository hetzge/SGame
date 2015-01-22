package de.hetzge.sgame.entity.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Container implements Serializable {

	private class Value {
		private int max;
		private int amount;
	}

	private final Map<IF_Item, Value> items = new HashMap<>();

	public boolean transfer(IF_Item item, int amount, Container to) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amounts can be transfered");
		}
		synchronized (this) {
			synchronized (to) {
				if (!this.can(item) || !to.can(item) || !this.has(item) || !to.canAddAmount(item, amount) || !this.hasAmount(item, amount)) {
					return false;
				}
				Value value = this.items.get(item);
				value.amount -= amount;

				Value toValue = to.items.get(item);
				toValue.amount += amount;
			}
		}
		return true;
	}

	public boolean hasAmount(IF_Item item, int amount) {
		return this.amount(item) >= amount;
	}

	public boolean canAddAmount(IF_Item item, int amount) {
		Value value = this.items.get(item);
		return this.can(item) && value.amount + amount <= value.max;
	}

	public boolean can(IF_Item item) {
		return this.items.get(item) != null;
	}

	public boolean has(IF_Item item) {
		return this.amount(item) > 0;
	}

	public int amount(IF_Item item) {
		Value value = this.items.get(item);
		if (value == null) {
			return 0;
		}
		return value.amount;
	}

}
