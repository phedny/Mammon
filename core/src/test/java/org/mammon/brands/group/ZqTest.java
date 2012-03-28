package org.mammon.brands.group;

import org.junit.Before;
import org.junit.Test;
import org.mammon.brands.Group;
import org.mammon.brands.Group.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		Element<Zq> zero = group.getZero();

		assertEquals(group, zero.getGroup());
	}

	@Test
	public void shouldBeAbleToReturnOneOfTheGroup() {
		Element<Zq> one = group.getOne();

		assertEquals(group, one.getGroup());
	}

}
