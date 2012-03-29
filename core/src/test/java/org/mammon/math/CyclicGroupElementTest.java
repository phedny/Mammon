package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.mammon.math.CyclicGroup.C;

import org.junit.Before;
import org.junit.Test;
import org.mammon.math.Group.Element;

public class CyclicGroupElementTest {
	private Element<CyclicGroup> identity;
	private Element<CyclicGroup> generator;

	@Before
	public void createElements() {
		Group<CyclicGroup> group = C(5);
		identity = group.getIdentity();
		generator = group.getGenerator();
	}

	@Test
	public void identityShouldBeIdentityForMultiplication() {
		assertEquals(identity, identity.multiply(identity));
		assertEquals(generator, generator.multiply(identity));
	}

	@Test
	public void generatorShouldBeTheGeneratorForMultiplication() {
		assertEquals(generator, identity.multiply(generator));
	}

	@Test
	public void inverseShouldMultiplyToIdentity() {
		assertEquals(identity, generator.multiply(generator.getInverse()));
	}
}
