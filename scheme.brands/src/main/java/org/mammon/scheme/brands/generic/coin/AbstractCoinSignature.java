package org.mammon.scheme.brands.generic.coin;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.coin.CoinSignature;

public class AbstractCoinSignature<G extends Group<G>, F extends FiniteField<F>> implements CoinSignature<G, F> {

	private final Group.Element<G> z;

	private final Group.Element<G> a;

	private final Group.Element<G> b;

	private final FiniteField.Element<F> r;

	public AbstractCoinSignature(Group.Element<G> z, Group.Element<G> a, Group.Element<G> b, FiniteField.Element<F> r) {
		this.z = z;
		this.a = a;
		this.b = b;
		this.r = r;
	}

	@Override
	public Group.Element<G> getValA() {
		return a;
	}

	@Override
	public Group.Element<G> getValB() {
		return b;
	}

	@Override
	public FiniteField.Element<F> getValR() {
		return r;
	}

	@Override
	public Group.Element<G> getValZ() {
		return z;
	}

}
