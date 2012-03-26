package org.mammon.brands.example;

import org.mammon.brands.AccountHolder;
import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group;
import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;

public class ExampleAccountHolder<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements AccountHolder<G, S, T, H, H0> {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	private final Element<G> blindedIdentity;

	public ExampleAccountHolder(BrandsSchemeSetup<G, S, T, H, H0> setup, ExampleBank<G, S, T, H, H0> bank) {
		this.setup = setup;
		privateKey = setup.getGroup().getRandomElement(null);
		publicKey = setup.getGenerators()[1].exponentiate(privateKey);
		blindedIdentity = publicKey.multiply(setup.getGenerators()[2]).exponentiate(bank.getPrivateKey());
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public Element<G> getPrivateKey() {
		return privateKey;
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
		if (obj == null || !(obj instanceof ExampleAccountHolder)) {
			return false;
		}
		ExampleAccountHolder other = (ExampleAccountHolder) obj;
		return setup.equals(other.setup) && blindedIdentity.equals(other.blindedIdentity);
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
		return "ExampleAccountHolder(" + setup.hashCode() + "," + blindedIdentity.toString() + ")";
	}

}
