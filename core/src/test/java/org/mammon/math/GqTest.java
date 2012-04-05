package org.mammon.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GqTest {
	private Group<Gq> group;

	@Test(expected = IllegalArgumentException.class)
	public void qShouldDividePminus1() {
		new Gq(12, 23);
	}

	@Before
	public void createGroup() {
		this.group = new Gq(11, 23);
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
