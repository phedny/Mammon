package org.mammon.math;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.mammon.math.util.Gcd;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.ReturnsEnclosing;

@AvailableAtRuntime(FiniteField.class)
public class Z implements Identifiable, FiniteField<Z> {

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
	public String getIdentity() {
		return "Z(" + q + ")";
	}

	@Override
	public FiniteField.Element<Z> getZero() {
		return element(BigInteger.ZERO);
	}

	public FiniteField.Element<Z> element(BigInteger element) {
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

	public class ZElement implements FiniteField.Element<Z> {

		private final BigInteger element;

		@FromPersistent(FiniteField.Element.class)
		public ZElement(@PersistAs("element") BigInteger element) {
			this.element = element.mod(q);
		}

		@Override
		@ReturnsEnclosing
		public Z getFiniteField() {
			return Z.this;
		}

		public BigInteger getElement() {
			return element;
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
			FiniteField.Element<Z> result = this.getFiniteField().getOne();
			FiniteField.Element<Z> power = this;
			BigInteger value = ((Z.ZElement) other).element;
			while (value.compareTo(BigInteger.ZERO) > 0) {
				if (value.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
					result = result.multiply(power);
				}
				power = power.multiply(power);
				value = value.divide(BigInteger.valueOf(2));
			}
			return result;
		}

		@Override
		public <G extends Group<G>> Group.Element<G> raise(Group.Element<G> groupElement) {
			Group.Element<G> result = groupElement.getGroup().getIdentityElement();
			for (BigInteger power = BigInteger.ZERO; power.compareTo(element) < 0; power = power.add(BigInteger.ONE)) {
				// TODO Use Binary Quadratation.
				result = result.multiply(groupElement);
			}
			return result;
		}

		@Override
		public String toString() {
			return element.toString();
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
