package org.mammon.math;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GqSetupFactoryTest {
	private GqSetupFactory factory;

	@Before
	public void createGqSetupFactory() {
		this.factory = GqSetupFactory.factory(11, 23);
	}

	@Test
	public void shouldReturnCorrectQandP() {
		GqSetup setup = factory.generateSetup();
		assertEquals(BigInteger.valueOf(11), setup.q);
		assertEquals(BigInteger.valueOf(23), setup.p);
	}

	@Test
	public void shouldReturnCorrectQandPOverAndOver() {
		GqSetup first = factory.generateSetup();
		GqSetup second = factory.generateSetup();
		assertTrue(first.q.equals(second.q));
		assertTrue(first.p.equals(second.p));
	}

	@Test
	public void shouldReturnDifferentGenerators() {
		GqSetup first = factory.generateSetup();
		GqSetup second = factory.generateSetup();
		assertFalse(first.generator.equals(second.generator));
	}
}
