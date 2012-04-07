package org.mammon.math;

import java.math.BigInteger;
import java.util.List;

import org.mammon.math.util.PrimeFactors;

public class GqSetupFactory {
	public static GqSetupFactory factory(int q, int p) {
		return factory(BigInteger.valueOf(q), BigInteger.valueOf(p));
	}

	public static GqSetupFactory factory(BigInteger q, BigInteger p) {
		return new GqSetupFactory(q, p);
	}

	private final BigInteger q;
	private final BigInteger p;
	private final BigInteger generator;
	private BigInteger primitiveRoot;
	private List<BigInteger> factorsOftotientP;

	private GqSetupFactory(BigInteger q, BigInteger p) {
		qShouldDivideTotientP(q, p);
		this.q = q;
		this.p = p;
		this.generator = createGenerator();

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

	public BigInteger createGenerator() {
		BigInteger primitiveRoot = getPrimitiveRoot();
		return primitiveRoot.modPow(totient(p).divide(q), p);
	}

	private BigInteger getPrimitiveRoot() {
		if (primitiveRoot == null) {
			primitiveRoot = generatePrimitiveRoot();
		}
		return primitiveRoot;
	}

	private BigInteger generatePrimitiveRoot() {
		BigInteger candidate = BigInteger.valueOf(2);
		while (!isPrimitiveRoot(candidate)) {
			candidate = candidate.add(BigInteger.ONE);
		}
		return candidate;
	}

	private boolean isPrimitiveRoot(BigInteger candidate) {
		List<BigInteger> factors = factorsOfTotientP();
		for (BigInteger factor : factors) {
			if (p.mod(factor).equals(BigInteger.ZERO)) {
				return false;
			}
		}
		return true;
	}

	private List<BigInteger> factorsOfTotientP() {
		if (factorsOftotientP == null) {
			factorsOftotientP = PrimeFactors.of(p.subtract(BigInteger.ONE));

		}
		return factorsOftotientP;
	}

	public GqSetup generateSetup() {
		return new GqSetup(q, p, generator);

	}
}
