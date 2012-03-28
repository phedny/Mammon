package org.mammon.sandbox.generic;

import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractBank<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I>
		implements Bank<G, S, T, H, H0>, Identifiable<I>, Transactable {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;
	private final Element<G> publicKey;

	protected AbstractBank(BrandsSchemeSetup<G, S, T, H, H0> setup, Element<G> publicKey) {
		this.setup = setup;
		this.publicKey = publicKey;
	}

	@Override
	public Element<G> getPublicKey() {
		return publicKey;
	}

	@Override
	public BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractBank<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractBank<?, ?, ?, ?, ?, ?> other = (AbstractBank<?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && publicKey.equals(other.publicKey);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, publicKey);
		return publicKey.hashCode();
	}

	@Override
	public String toString() {
		return "ExampleBank(" + setup.hashCode() + "," + publicKey.toString() + ")";
	}

}