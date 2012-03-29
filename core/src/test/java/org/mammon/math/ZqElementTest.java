package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.mammon.math.Zq.Z;

import org.junit.Before;
import org.junit.Test;
import org.mammon.math.Group;
import org.mammon.math.Zq;
import org.mammon.math.Group.Element;

public class ZqElementTest {
	private Element<Zq> identity;
	private Element<Zq> generator;

	@Before
	public void createElements() {
		Group<Zq> group = Z(5);
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
