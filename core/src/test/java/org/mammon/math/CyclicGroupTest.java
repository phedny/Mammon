package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mammon.math.Group.Element;

public class CyclicGroupTest {
	private Group<CyclicGroup> group;

	@Before
	public void createGroup() {
		group = CyclicGroup.C(5);
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(group);
	}

	@Test
	public void shouldBeAbleToReturnZeroOfTheGroup() {
		Element<CyclicGroup> zero = group.getIdentity();

		assertEquals(group, zero.getGroup());
	}

	@Test
	public void shouldBeAbleToReturnOneOfTheGroup() {
		Element<CyclicGroup> one = group.getGenerator();

		assertEquals(group, one.getGroup());
	}

}
