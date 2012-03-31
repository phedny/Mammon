package org.mammon.math.util;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GcdTest {
	private final BigInteger a;
	private final BigInteger b;
	private final BigInteger expected;

	public GcdTest(Integer a, Integer b, Integer expected){
		this.a = BigInteger.valueOf(a);
		this.b = BigInteger.valueOf(b);
		this.expected = BigInteger.valueOf(expected);
	}

	@Test
	public void shouldCalculateTheExtendedGcd() {
		BigInteger[] result = Gcd.of(a, b);

		assertEquals(expected, result[0]);
		assertEquals(result[0], result[1].multiply(a).add(result[2].multiply(b)));
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{37, 51, 1});
		data.add(new Object[]{44, 11, 11});
		data.add(new Object[]{210, 15, 15});
		return data;
	}
}
