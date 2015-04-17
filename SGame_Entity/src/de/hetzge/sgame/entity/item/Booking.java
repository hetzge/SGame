package de.hetzge.sgame.entity.item;

public class Booking {
	final IF_Item item;
	final int amount;
	final Container from;
	final Container to;

	Booking(IF_Item item, int amount, Container from, Container to) {
		if (item == null || from == null || to == null) {
			throw new IllegalArgumentException("Invalid null parameter. No null values allowed: item->" + item + ", from->" + from + ", to->" + to);
		}
		if (from == to) {
			throw new IllegalArgumentException("No booking to self container allowed.");
		}
		if (amount <= 0) {
			throw new IllegalArgumentException("No amount smaller or equal 0 allowed: " + amount);
		}
		this.item = item;
		this.amount = amount;
		this.from = from;
		this.to = to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.amount;
		result = prime * result + ((this.from == null) ? 0 : this.from.hashCode());
		result = prime * result + ((this.item == null) ? 0 : this.item.hashCode());
		result = prime * result + ((this.to == null) ? 0 : this.to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Booking other = (Booking) obj;
		if (this.amount != other.amount) {
			return false;
		}
		if (this.from == null) {
			if (other.from != null) {
				return false;
			}
		} else if (!this.from.equals(other.from)) {
			return false;
		}
		if (this.item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!this.item.equals(other.item)) {
			return false;
		}
		if (this.to == null) {
			if (other.to != null) {
				return false;
			}
		} else if (!this.to.equals(other.to)) {
			return false;
		}
		return true;
	}

}