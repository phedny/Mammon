package org.mammon.scheme.brands.generic.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.util.HashCodeUtil;
import org.mammon.util.messaging.AbstractTransactable;

public abstract class AbstractBank<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractTransactable implements Bank<G, F, T, H, H0>, Identifiable, Transactable {

	private final BrandsSchemeSetup<G, F, T, H, H0> setup;
	private final Group.Element<G> publicKey;

	protected AbstractBank(BrandsSchemeSetup<G, F, T, H, H0> setup,
			Group.Element<G> publicKey) {
		this.setup = setup;
		this.publicKey = publicKey;
	}

	@Override
	public Group.Element<G> getPublicKey() {
		return publicKey;
	}

	@Override
	public BrandsSchemeSetup<G, F, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractBank<?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractBank<?, ?, ?, ?, ?> other = (AbstractBank<?, ?, ?, ?, ?>) obj;
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
		return "ExampleBank(" + setup.hashCode() + "," + publicKey.toString()
				+ ")";
	}

}