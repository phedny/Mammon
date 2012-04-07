package org.mammon.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mammon.math.GqSetupFactory.factory;

public class GqTest {
	private Group<Gq> group;

	@Test(expected = IllegalArgumentException.class)
	public void qShouldDividePminus1() {
		new Gq(factory(12, 23).generateSetup());
	}

	@Before
	public void createGroup() {
		this.group = new Gq(factory(11, 23).generateSetup());
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(group);
	}

	@Test
	public void shouldBeAbleToReturnTheIdentityOfTheGroup() {
		Group.Element<Gq> identity = group.getIdentityElement();

		assertEquals(group, identity.getGroup());
	}

	@Test
	public void shouldBeAbleToReturnTheGeneratorOfTheGroup() {
		Group.Element<Gq> generator = group.getGenerator();

		assertEquals(group, generator.getGroup());
	}

	@Test
	public void shouldBeAbleToReturnARandomElementOfTheGroup() {
		Group.Element<Gq> element = group.getRandomElement();

		assertEquals(group, element.getGroup());
	}
}
