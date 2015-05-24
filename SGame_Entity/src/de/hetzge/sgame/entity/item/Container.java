package de.hetzge.sgame.entity.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Container implements Serializable {

	private class Value implements Serializable {
		private int max;
		private int amount;
	}

	private final Map<IF_Item, Value> items = new HashMap<>();
	private final List<Booking> bookings = new LinkedList<>();

	public synchronized boolean transfer(Booking booking) {
		if (booking.from != this) {
			throw new IllegalArgumentException("You can only transfer bookings from self container.");
		}
		if (!this.hasBooking(booking) || !booking.to.hasBooking(booking)) {
			throw new IllegalArgumentException("Try to transfer a booking that not exists for container.");
		}
		synchronized (booking.to) {
			boolean transferSuccessful = this.transfer(booking.item, booking.amount, booking.to, true);
			if (transferSuccessful) {
				this.bookings.remove(booking);
				booking.to.bookings.remove(booking);
			}
			return transferSuccessful;
		}
	}

	public synchronized boolean transfer(IF_Item item, int amount, Container to) {
		return this.transfer(item, amount, to, false);
	}

	public synchronized boolean transfer(IF_Item item, int amount, Container to, boolean booking) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amounts can be transfered");
		}
		synchronized (to) {
			if (!this.can(item) || !to.can(item) || !this.has(item) || (booking ? !this.hasBooking(item, amount, this, to) : !to.canAddAmount(item, amount)) || (booking ? !this.hasBooking(item, amount, this, to) : !this.hasAmountAvailable(item, amount))) {
				return false;
			}
			Value value = this.items.get(item);
			value.amount -= amount;

			Value toValue = to.items.get(item);
			toValue.amount += amount;
		}

		return true;
	}

	private boolean hasBooking(IF_Item item, int amount, Container from, Container to) {
		return this.hasBooking(new Booking(item, amount, from, to));
	}

	private boolean hasBooking(Booking booking) {
		return this.bookings.contains(booking);
	}

	public synchronized Booking book(IF_Item item, int amount, Container to) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amounts can be booked");
		}
		synchronized (to) {
			if (!this.has(item) || !this.hasAmountAvailable(item, amount)) {
				return null;
			}
			if (!to.can(item) || !to.canAddAmount(item, amount)) {
				return null;
			}

			Booking booking = new Booking(item, amount, this, to);
			this.addBooking(booking);
			to.addBooking(booking);

			return booking;
		}
	}

	private synchronized void addBooking(Booking booking) {
		this.bookings.add(booking);
	}

	private synchronized void removeBooking(Booking booking) {
		this.bookings.remove(booking);
	}

	public synchronized boolean hasAmountAvailable(IF_Item item, int amount) {
		return this.has(item) && this.amount(item) - this.bookedFromAmount(item) >= amount;
	}

	public synchronized boolean canAddAmount(IF_Item item, int amount) {
		Value value = this.items.get(item);
		return this.can(item) && this.amount(item) + this.bookedToAmount(item) + amount <= value.max;
	}

	/**
	 * Returns the amount of a given item is booked from this container.
	 */
	private int bookedFromAmount(IF_Item item) {
		int amount = 0;
		for (Booking booking : this.bookings) {
			if (booking.from == this && booking.item.equals(item)) {
				amount += booking.amount;
			}
		}
		return amount;
	}

	/**
	 * Returns the amount of a given item is reserved to bring to this
	 * container.
	 */
	private int bookedToAmount(IF_Item item) {
		int amount = 0;
		for (Booking booking : this.bookings) {
			if (booking.to == this && booking.item.equals(item)) {
				amount += booking.amount;
			}
		}
		return amount;
	}

	public synchronized boolean can(IF_Item item) {
		return this.items.get(item) != null;
	}

	public synchronized boolean has(IF_Item item) {
		return this.amount(item) > 0;
	}

	public synchronized int amount(IF_Item item) {
		Value value = this.items.get(item);
		if (value == null) {
			return 0;
		}
		return value.amount;
	}

	public synchronized void set(IF_Item item, int amount, int max) {
		Value value = this.getOrCreateValue(item);
		value.amount = amount;
		value.max = max;
	}

	public synchronized void set(IF_Item item, int amountAndMax) {
		this.set(item, amountAndMax, amountAndMax);
	}

	public synchronized void setAmount(IF_Item item, int amount) {
		Value value = this.getOrCreateValue(item);
		value.amount = amount;
	}

	public synchronized void setMax(IF_Item item, int max) {
		Value value = this.getOrCreateValue(item);
		value.max = max;
	}

	private Value getOrCreateValue(IF_Item item) {
		Value value = this.items.get(item);
		if (value == null) {
			value = new Value();
			this.items.put(item, value);
		}
		return value;
	}

	public synchronized void unchain() {
		for (Booking booking : this.bookings) {
			booking.to.removeBooking(booking);
		}
		this.bookings.clear();
	}

}
