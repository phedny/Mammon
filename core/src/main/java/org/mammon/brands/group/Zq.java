package org.mammon.brands.group;

import java.math.BigDecimal;
import java.util.Random;

import org.mammon.brands.Group;

public class Zq implements Group<Zq> {

	public static Group<Zq> Z(long order) {
		return new Zq(BigDecimal.valueOf(order));
	}

	public Zq(BigDecimal valueOf) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element<Zq> getZero() {
		return element(0L);
	}

	private Element<Zq> element(long element) {
		return new ZqElement(BigDecimal.valueOf(element));
	}

	@Override
	public Element<Zq> getOne() {
		return element(1L);
	}

	@Override
	public Element<Zq> getRandomElement(Random random) {
		// TODO Auto-generated method stub
		return null;
	}

	class ZqElement implements Element<Zq> {

		public ZqElement(BigDecimal element) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public Zq getGroup() {
			return Zq.this;
		}

		@Override
		public Element<Zq> getInverse() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element<Zq> add(Element<Zq> other) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element<Zq> multiply(Element<Zq> other) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element<Zq> exponentiate(Element<Zq> exponent) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
