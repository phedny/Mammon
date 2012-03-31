package org.mammon.math;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.mammon.math.util.Gcd;

public class Z implements FiniteField<Z> {

	private final Random random;
	private final BigInteger q;

	public Z(int q) {
		this(q, new SecureRandom());
	}

	public Z(int q, Random random) {
		this(BigInteger.valueOf(q), random);
	}

	public Z(BigInteger q) {
		this(q, new SecureRandom());
	}

	public Z(BigInteger q, Random random) {
		this.q = q;
		this.random = new Random();
	}

	@Override
	public FiniteField.Element<Z> getZero() {
		return element(BigInteger.ZERO);
	}

	private FiniteField.Element<Z> element(BigInteger element) {
		return new ZElement(element);
	}

	public FiniteField.Element<Z> element(Integer element) {
		return element(BigInteger.valueOf(element));
	}

	@Override
	public FiniteField.Element<Z> getOne() {
		return element(BigInteger.ONE);
	}

	@Override
	public FiniteField.Element<Z> getRandomElement() {
		// TODO Generate a random BigInteger
		return element(random.nextInt(q.intValue()));
	}

	class ZElement implements FiniteField.Element<Z> {

		private final BigInteger element;

		public ZElement(BigInteger element) {
			this.element = element.mod(q);
		}

		@Override
		public Z getFiniteField() {
			return Z.this;
		}

		@Override
		public FiniteField.Element<Z> add(FiniteField.Element<Z> other) {
			return element(this.element.add(((ZElement) other).element));
		}

		@Override
		public FiniteField.Element<Z> getOpposite() {
			return element(this.element.negate());
		}

		@Override
		public FiniteField.Element<Z> multiply(FiniteField.Element<Z> other) {
			return element(this.element.multiply(((ZElement) other).element));
		}

		@Override
		public FiniteField.Element<Z> getInverse() {
			BigInteger[] gcd = Gcd.of(element, q);
			return element(gcd[1]);
		}

		@Override
		public FiniteField.Element<Z> exponentiate(FiniteField.Element<Z> other) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getFiniteField().hashCode();
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
			ZElement other = (ZElement) obj;
			if (!getFiniteField().equals(other.getFiniteField()))
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
