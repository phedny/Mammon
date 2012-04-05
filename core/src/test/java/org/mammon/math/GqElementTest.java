package org.mammon.math;

import org.junit.Before;
import org.junit.Test;
import org.mammon.math.Group.Element;

import static org.junit.Assert.assertEquals;

public class GqElementTest {

	private Group<Gq> group;
	private Element<Gq> identity;
	private Element<Gq> generator;

	@Before
	public void createGroup() {
		group = new Gq(11, 23);
		identity = group.getIdentityElement();
		generator = group.getGenerator();
	}

	@Test
	public void identityShouldBeIdentityForMultiplication() {
		assertEquals(identity, identity.multiply(identity));
		assertEquals(generator, generator.multiply(identity));
	}

	@Test
	public void inverseShouldMultiplyToIdentity() {
		assertEquals(identity, generator.multiply(generator.getInverse()));
	}
}
