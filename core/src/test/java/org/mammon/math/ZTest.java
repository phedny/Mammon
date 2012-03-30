package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ZTest {
	FiniteField<Z> field;

	@Before
	public void createFiniteField() {
		field = new Z(11);
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(field);
	}

	@Test
	public void shouldBeAbleToReturnTheZeroOfTheFiniteField() {
		FiniteField.Element<Z> zero = field.getZero();

		assertEquals(field, zero.getFiniteField());
	}

	@Test
	public void shouldBeAbleToReturnTheOneOfTheFiniteField() {
		FiniteField.Element<Z> one = field.getOne();

		assertEquals(field, one.getFiniteField());
	}
}
