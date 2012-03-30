package org.mammon.math;

import java.math.BigInteger;

public class Z implements FiniteField<Z> {

	private final BigInteger q;

	public Z(int q) {
		this(BigInteger.valueOf(q));
	}

	public Z(BigInteger q) {
		this.q = q;
	}

	@Override
	public FiniteField.Element<Z> getZero() {
		return element(BigInteger.ZERO);
	}

	private FiniteField.Element<Z> element(BigInteger element) {
		return new ZElement(element);
	}

	@Override
	public FiniteField.Element<Z> getOne() {
		return element(BigInteger.ONE);
	}

	@Override
	public FiniteField.Element<Z> getRandomElement() {
		// TODO Auto-generated method stub
		return null;
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
			return element(this.element.add(((ZElement)other).element));
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FiniteField.Element<Z> exponentiate(FiniteField.Element<Z> other) {
			// TODO Auto-generated method stub
			return null;
		}

		private Z getOuterType() {
			return Z.this;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((element == null) ? 0 : element.hashCode());
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
			if (!getOuterType().equals(other.getOuterType()))
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
