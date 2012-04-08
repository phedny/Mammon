package org.mammon.math;

import java.math.BigInteger;

public class GqSetup {
	public final BigInteger p;
	public final BigInteger q;
	public final BigInteger generator;

	public GqSetup(BigInteger q, BigInteger p, BigInteger generator) {
		qShouldDivideTotientP(q, p);
		this.q = q;
		this.p = p;
		this.generator = generator;
	}

	private void qShouldDivideTotientP(BigInteger q, BigInteger p) {
		BigInteger[] divmod = totient(p).divideAndRemainder(q);
		if (!divmod[1].equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException("q should divide p-1");
		}
	}

	private BigInteger totient(BigInteger p) {
		return p.subtract(BigInteger.ONE);
	}
}
