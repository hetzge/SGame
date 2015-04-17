package de.hetzge.sgame.entity.item;

import org.junit.Assert;
import org.junit.Test;

public class ContainerTest {

	private class Item implements IF_Item {
	}

	@Test
	public void testTransfer() {
		Container containerA = new Container();
		Container containerB = new Container();

		Item itemA = new Item();
		Item itemB = new Item();

		Assert.assertFalse(containerA.transfer(itemA, 10, containerB));

		containerA.set(itemA, 10, 10);
		Assert.assertFalse(containerA.transfer(itemA, 10, containerB));

		containerB.set(itemA, 0, 10);
		Assert.assertTrue(containerA.transfer(itemA, 10, containerB));
		Assert.assertEquals(containerA.amount(itemA), 0);
		Assert.assertEquals(containerB.amount(itemA), 10);

		containerA.set(itemA, 0, 9);
		Assert.assertFalse(containerB.transfer(itemA, 10, containerA));

		containerA.set(itemB, 5, 5);
		Assert.assertFalse(containerA.transfer(itemB, 5, containerB));
		Assert.assertEquals(containerA.amount(itemB), 5);
	}

	@Test
	public void testBooking() {
		Container containerA = new Container();
		Container containerB = new Container();

		Item itemA = new Item();

		Assert.assertEquals(containerA.book(itemA, 10, containerB), null);

		containerA.set(itemA, 10, 10);
		Assert.assertEquals(containerA.book(itemA, 10, containerB), null);

		containerB.set(itemA, 0, 10);
		Booking booking = containerA.book(itemA, 10, containerB);
		Assert.assertTrue(booking != null);

		Assert.assertEquals(containerA.book(itemA, 10, containerB), null);
		Assert.assertEquals(containerB.amount(itemA), 0);
		Assert.assertEquals(containerA.amount(itemA), 10);
		Assert.assertFalse(containerA.transfer(itemA, 10, containerB));

		Assert.assertTrue(containerA.transfer(booking));
		Assert.assertEquals(containerA.amount(itemA), 0);
		Assert.assertEquals(containerB.amount(itemA), 10);

		try {
			Assert.assertFalse(containerA.transfer(booking));
			Assert.assertTrue(false);
		} catch (IllegalArgumentException ex) {
			Assert.assertTrue(true);
		}

		try {
			Assert.assertFalse(containerB.transfer(booking));
			Assert.assertTrue(false);
		} catch (IllegalArgumentException ex) {
			Assert.assertTrue(true);
		}
	}
}
