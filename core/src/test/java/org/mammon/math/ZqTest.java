package org.mammon.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mammon.math.Group;
import org.mammon.math.Zq;
import org.mammon.math.Group.Element;

public class ZqTest {
	private Group<Zq> group;

	@Before
	public void createGroup() {
		group = Zq.Z(5);
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(group);
	}

	@Test
	public void shouldBeAbleToReturnZeroOfTheGroup() {
		Element<Zq> zero = group.getIdentity();

		assertEquals(group, zero.getGroup());
	}

	@Test
	public void shouldBeAbleToReturnOneOfTheGroup() {
		Element<Zq> one = group.getGenerator();

		assertEquals(group, one.getGroup());
	}

}
