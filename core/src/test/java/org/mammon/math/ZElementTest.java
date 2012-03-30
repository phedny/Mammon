package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ZElementTest {

	private FiniteField<Z> field;
	private FiniteField.Element<Z> zero;
	private FiniteField.Element<Z> one;

	@Before
	public void createFiniteField() {
		field = new Z(11);
		zero = field.getZero();
		one = field.getOne();
	}

	public void zeroShouldDifferFromOne() {
		assertFalse(one.equals(zero));
	}

	@Test
	public void zeroShouldBeIdentityForAddition() {
		assertEquals(zero, zero.add(zero));
		assertEquals(one, one.add(zero));
	}

	@Test
	public void oneShouldBeIdentityForMulitplication() {
		assertEquals(zero, zero.multiply(one));
		assertEquals(one, one.multiply(one));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void oppositesShouldSumToZero() {
		for (FiniteField.Element<Z> element : new FiniteField.Element[]{zero, one}) {
			assertEquals(zero, element.add(element.getOpposite()));
		}
	}
}
