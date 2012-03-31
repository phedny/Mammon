package org.mammon.math.util;

import java.math.BigInteger;

public class Gcd {
	/**
	 * Calculates the extended greatest common divisor of <code>a</code> and <code>b</code>.
	 *
	 * @param a
	 * @param b
	 * @return an array [gcd, x, y] such that gcd = a * x + b * y
	 */
	public static BigInteger[] of(BigInteger a, BigInteger b) {
		BigInteger x0 = BigInteger.ONE; BigInteger y0 = BigInteger.ZERO;
		BigInteger x1 = BigInteger.ZERO; BigInteger y1 = BigInteger.ONE;
		while(!b.equals(BigInteger.ZERO)) {
			BigInteger[] divmod = a.divideAndRemainder(b);
			BigInteger xt = x0; BigInteger yt = y0;
			x0 = x1; y0 = y1;
			x1 = xt.subtract(divmod[0].multiply(x1)); y1 = yt.subtract(divmod[0].multiply(y1));
			a = b; b = divmod[1];
		}
		return new BigInteger[]{a, x0, y0};
	}

}
