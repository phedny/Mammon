package org.mammon.math.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PrimeFactorsTest {
	private final BigInteger n;
	private final List<BigInteger> expectedFactors = new ArrayList<BigInteger>();

	public PrimeFactorsTest(Integer n, Integer[] expectedFactors) {
		this.n = BigInteger.valueOf(n);
		for (Integer factor : expectedFactors) {
			this.expectedFactors.add(BigInteger.valueOf(factor));
		}
	}

	@Test
	public void shouldCorrectlyDeterminePrimeFactors() {
		List<BigInteger> factors = PrimeFactors.of(n);

		assertEquals(expectedFactors, factors);
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { 2, new Integer[] { 2 } });
		data.add(new Object[] { 3, new Integer[] { 3 } });
		data.add(new Object[] { 4, new Integer[] { 2 } });
		data.add(new Object[] { 5, new Integer[] { 5 } });
		data.add(new Object[] { 6, new Integer[] { 2, 3 } });
		data.add(new Object[] { 7, new Integer[] { 7 } });
		data.add(new Object[] { 8, new Integer[] { 2 } });
		data.add(new Object[] { 9, new Integer[] { 3 } });
		data.add(new Object[] { 10, new Integer[] { 2, 5 } });
		return data;
	}
}
