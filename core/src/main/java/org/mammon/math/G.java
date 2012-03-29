package org.mammon.math;

import java.math.BigInteger;

import org.mammon.scheme.brands.rand.RandomGenerator;

public class G implements Group<G> {

	private final BigInteger q;
	private final BigInteger p;

	public G(int q, int p) {
		this(BigInteger.valueOf(q), BigInteger.valueOf(p));
	}

	public G(BigInteger q, BigInteger p) {
		qShouldDividePminus1(q, p);
		this.q = q;
		this.p = p;
	}

	private void qShouldDividePminus1(BigInteger q, BigInteger p) {
		BigInteger[] divmod = p.subtract(BigInteger.ONE).divideAndRemainder(q);
		if (!divmod[1].equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException("q should divide p-1");
		}
	}

	@Override
	public Group.Element<G> getIdentity() {
		return element(BigInteger.ONE);
	}

	private Group.Element<G> element(BigInteger element) {
		return new Element(element);
	}

	@Override
	public Group.Element<G> getGenerator() {
		return null;
	}

	@Override
	public Group.Element<G> getRandomElement(RandomGenerator randomGenerator) {
		// TODO Auto-generated method stub
		return null;
	}

	class Element implements Group.Element<G> {

		public Element(BigInteger element) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public G getGroup() {
			return G.this;
		}

		@Override
		public Group.Element<G> getInverse() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Group.Element<G> multiply(Group.Element<G> other) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <F extends FiniteField<F>> Group.Element<G> exponentiate(FiniteField.Element<F> exponent) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
