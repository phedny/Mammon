package org.mammon.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ZElementTest {

	private final int q = 11;
	private FiniteField<Z> field;
	private FiniteField.Element<Z> zero;
	private FiniteField.Element<Z> one;

	@Before
	public void createFiniteField() {
		field = new Z(q);
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

	@Test
	public void oppositesShouldSumToZero() {
		Z cast = (Z) field;
		for (FiniteField.Element<Z> element : new FiniteField.Element[] { zero, one, cast.element(2), cast.element(3) }) {
			assertEquals(zero, element.add(element.getOpposite()));
		}
	}

	@Test
	public void inversesShouldMultiplyToOne() {
		Z cast = (Z) field;
		for (FiniteField.Element<Z> element : new FiniteField.Element[] { one, cast.element(2), cast.element(3) }) {
			assertEquals(one, element.multiply(element.getInverse()));
		}
	}

	@Test
	public void additionShouldHaveOrderQ() {
		FiniteField.Element<Z> result = zero;
		for (int i = 0; i < q; i++) {
			result = result.add(one);
		}
		assertEquals(zero, result);
	}

	@Test
	public void mulitplicationShouldHaveOrderTotientQ() {
		FiniteField.Element<Z> result = one;
		FiniteField.Element<Z> generator = ((Z) field).element(2);
		for (int i = 0; i < q - 1; i++) {
			result = result.multiply(generator);
		}
		assertEquals(one, result);
	}
}
