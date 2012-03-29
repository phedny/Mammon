package org.mammon.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GTest {
	private Group<G> group;

	@Test(expected = IllegalArgumentException.class)
	public void qShouldDividePminus1() {
		new G(12, 23);
	}

	@Before
	public void createGroup() {
		this.group = new G(11, 23);
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(group);
	}

	@Test
	public void shouldBeAbleToReturnTheIdentityOfTheGroup() {
		Group.Element<G> identity = group.getIdentity();

		assertEquals(group, identity.getGroup());
	}
}
