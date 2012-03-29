package org.mammon.math.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {

	public static List<BigInteger> of(BigInteger n) {
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		BigInteger divisor = BigInteger.valueOf(2);
		while (divisor.compareTo(n) <= 0) {
			if (n.mod(divisor).equals(BigInteger.ZERO)) {
				factors.add(divisor);
				while (n.mod(divisor).equals(BigInteger.ZERO)) {
					n = n.divide(divisor);
				}
			}
			divisor = divisor.add(BigInteger.ONE);
		}
		return factors;
	}

}
