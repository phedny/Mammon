package org.mammon.sandbox.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolder;

public abstract class AbstractAccountHolder<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		implements AccountHolder<G, F, S, T, H, H0>, Identifiable<I>,
		Transactable {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;
	private final Element<G> blindedIdentity;

	public AbstractAccountHolder(BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			Element<G> blindedIdentity) {
		this.setup = setup;
		this.blindedIdentity = blindedIdentity;
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public BrandsSchemeSetup<G, F, S, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null
				|| !(obj instanceof AbstractAccountHolder<?, ?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractAccountHolder<?, ?, ?, ?, ?, ?, ?> other = (AbstractAccountHolder<?, ?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup)
				&& blindedIdentity.equals(other.blindedIdentity);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, blindedIdentity);
		return hashCode;
	}

	@Override
	public String toString() {
		return "ExampleAccountHolder(" + setup.hashCode() + ","
				+ blindedIdentity.toString() + ")";
	}

}