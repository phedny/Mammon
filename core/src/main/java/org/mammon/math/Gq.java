package org.mammon.math;

import java.math.BigInteger;

import org.mammon.math.util.Gcd;

public class Gq implements Group<Gq> {

	private final BigInteger q;
	private final BigInteger p;
	private final Group.Element<Gq> generator;

	public Gq(GqSetup setup) {
		this.q = setup.q;
		this.p = setup.p;
		this.generator = element(setup.generator);
	}

	@Override
	public Group.Element<Gq> getIdentityElement() {
		return element(BigInteger.ONE);
	}

	private Group.Element<Gq> element(BigInteger element) {
		return new GqElement(element);
	}

	@Override
	public Group.Element<Gq> getGenerator() {
		return generator;
	}

	@Override
	public Group.Element<Gq> getRandomElement() {
		Z field = new Z(q);
		return getGenerator().exponentiate(field.getRandomElement());
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
