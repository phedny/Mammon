package org.mammon.sandbox.generic.shop;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.sandbox.generic.messaging.AbstractTransactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.shop.Shop;

public class AbstractShop<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransactable<I> implements Shop<G, F, I, T, H, H0> {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;
	private final I identity;

	public AbstractShop(BrandsSchemeSetup<G, F, I, T, H, H0> setup, I identity) {
		this.setup = setup;
		this.identity = identity;
	}

	@Override
	public I getIdentity() {
		return identity;
	}

	@Override
	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractShop<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractShop<?, ?, ?, ?, ?, ?> other = (AbstractShop<?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && identity.equals(other.identity);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, identity);
		return hashCode;
	}

	@Override
	public String toString() {
		return "ExampleShop(" + setup.hashCode() + "," + identity.toString() + ")";
	}

}