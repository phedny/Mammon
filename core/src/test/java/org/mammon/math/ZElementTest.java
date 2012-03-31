package org.mammon.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ZElementTest {

	private final int q;
	private FiniteField<Z> field;
	private FiniteField.Element<Z> zero;
	private FiniteField.Element<Z> one;

	public ZElementTest(int q) {
		this.q = q;
	}

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

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { 11 });
		data.add(new Object[] { 13 });
		data.add(new Object[] { 17 });
		data.add(new Object[] { 19 });
		return data;
	}
}
