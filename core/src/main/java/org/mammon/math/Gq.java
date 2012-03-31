package org.mammon.math;

import java.math.BigInteger;
import java.util.List;

import org.mammon.math.util.Gcd;
import org.mammon.math.util.PrimeFactors;

public class Gq implements Group<Gq> {

	private final BigInteger q;
	private final BigInteger p;
	private Group.Element<Gq> primitiveRoot;
	private List<BigInteger> factorsOftotientP;
	private Group.Element<Gq> generator;

	public Gq(int q, int p) {
		this(BigInteger.valueOf(q), BigInteger.valueOf(p));
	}

	public Gq(BigInteger q, BigInteger p) {
		qShouldDivideTotientP(q, p);
		this.q = q;
		this.p = p;
	}

	private void qShouldDivideTotientP(BigInteger q, BigInteger p) {
		BigInteger[] divmod = p.subtract(BigInteger.ONE).divideAndRemainder(q);
		if (!divmod[1].equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException("q should divide p-1");
		}
	}

	@Override
	public Group.Element<Gq> getIdentity() {
		return element(BigInteger.ONE);
	}

	private Group.Element<Gq> element(BigInteger element) {
		return new GqElement(element);
	}

	@Override
	public Group.Element<Gq> getGenerator() {
		if (generator == null) {
			Z field = new Z(p);
			Group.Element<Gq> primitiveRoot = getPrimitiveRoot();
			generator = primitiveRoot.exponentiate(field.element(p.subtract(BigInteger.ONE)).multiply(
				field.element(q).getInverse()));
		}
		return generator;
	}

	private Group.Element<Gq> getPrimitiveRoot() {
		if (primitiveRoot == null) {
			primitiveRoot = generatePrimitiveRoot();
		}
		return primitiveRoot;
	}

	private Group.Element<Gq> generatePrimitiveRoot() {
		BigInteger candidate = BigInteger.valueOf(2);
		while (!isPrimitiveRoot(candidate)) {
			candidate = candidate.add(BigInteger.ONE);
		}
		return element(candidate);
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

	@Override
	public Group.Element<Gq> getRandomElement() {
		// TODO Auto-generated method stub
		return null;
	}

	class GqElement implements Group.Element<Gq> {

		private final BigInteger element;

		public GqElement(BigInteger element) {
			this.element = element.mod(p);
		}

		@Override
		public Gq getGroup() {
			return Gq.this;
		}

		@Override
		public Group.Element<Gq> getInverse() {
			BigInteger[] gcd = Gcd.of(element, p);
			return element(gcd[1]);
		}

		@Override
		public Group.Element<Gq> multiply(Group.Element<Gq> other) {
			return element(this.element.multiply(((GqElement) other).element));
		}

		@Override
		public <F extends FiniteField<F>> Group.Element<Gq> exponentiate(FiniteField.Element<F> exponent) {
			return exponent.raise(this);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getGroup().hashCode();
			result = prime * result + ((element == null) ? 0 : element.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GqElement other = (GqElement) obj;
			if (!getGroup().equals(other.getGroup()))
				return false;
			if (element == null) {
				if (other.element != null)
					return false;
			} else if (!element.equals(other.element))
				return false;
			return true;
		}
	}

}
