package org.mammon.math;

import java.math.BigDecimal;

public class CyclicGroup implements Group<CyclicGroup> {

	public static Group<CyclicGroup> C(long order) {
		return new CyclicGroup(BigDecimal.valueOf(order));
	}

	public CyclicGroup(BigDecimal valueOf) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element<CyclicGroup> getIdentity() {
		return element(0L);
	}

	private Element<CyclicGroup> element(long element) {
		return element(BigDecimal.valueOf(element));
	}

	private ZqElement element(BigDecimal element) {
		return new ZqElement(element);
	}

	@Override
	public Element<CyclicGroup> getGenerator() {
		return element(1L);
	}

	@Override
	public Element<CyclicGroup> getRandomElement() {
		// TODO Auto-generated method stub
		return null;
	}

	class ZqElement implements Element<CyclicGroup> {
		private final BigDecimal element;

		public ZqElement(BigDecimal element) {
			this.element = element;
		}

		@Override
		public CyclicGroup getGroup() {
			return CyclicGroup.this;
		}

		@Override
		public Element<CyclicGroup> getInverse() {
			return element(this.element.negate());
		}

		@Override
		public Element<CyclicGroup> multiply(Element<CyclicGroup> other) {
			return element(this.element.add(((ZqElement) other).element));
		}

		@Override
		public <F extends FiniteField<F>> Element<CyclicGroup> exponentiate(
				FiniteField.Element<F> exponent) {
			return null;
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
			ZqElement other = (ZqElement) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (element == null) {
				if (other.element != null)
					return false;
			} else if (!element.equals(other.element))
				return false;
			return true;
		}

		private CyclicGroup getOuterType() {
			return CyclicGroup.this;
		}

	}
}
