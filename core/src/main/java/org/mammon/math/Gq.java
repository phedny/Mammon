package org.mammon.math;

import java.math.BigInteger;

import org.mammon.math.util.Gcd;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.ReturnsEnclosing;

@AvailableAtRuntime(Group.class)
public class Gq implements Identifiable, Group<Gq> {

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
	public String getIdentity() {
		return "Gq(" + q + "," + p + "," + generator + ")";
	}

	@Override
	public Group.Element<Gq> getRandomElement() {
		Z field = new Z(q);
		return getGenerator().exponentiate(field.getRandomElement());
	}

	public class GqElement implements Group.Element<Gq> {

		private final BigInteger element;

		@FromPersistent(Group.Element.class)
		public GqElement(@PersistAs("element") BigInteger element) {
			this.element = element.mod(p);
		}

		@Override
		@ReturnsEnclosing
		public Gq getGroup() {
			return Gq.this;
		}
		
		public BigInteger getElement() {
			return element;
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
		public String toString() {
			return element.toString();
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
