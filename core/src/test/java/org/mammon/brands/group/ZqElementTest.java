package org.mammon.brands.group;

import org.junit.Before;
import org.junit.Test;
import org.mammon.brands.Group;
import org.mammon.brands.Group.Element;

import static org.junit.Assert.assertEquals;

import static org.mammon.brands.group.Zq.Z;

public class ZqElementTest {
	private Element<Zq> zero;
	private Element<Zq> one;

	@Before
	public void createElements() {
		Group<Zq> group = Z(5);
		zero = group.getZero();
		one = group.getOne();
	}

	@Test
	public void zeroShouldBeIdentityForAddition() {
		assertEquals(zero, zero.add(zero));
		assertEquals(one, one.add(zero));
	}

	@Test
	public void oneShouldBeTheGeneratorForAddition() {
		assertEquals(one, zero.add(one));
	}

	@Test
	public void inverseShouldAddToZero() {
		assertEquals(zero, one.add(one.getInverse()));
	}
}
