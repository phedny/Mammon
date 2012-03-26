package org.mammon.brands.example;

import org.mammon.brands.Bank;
import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group;
import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;

public class ExampleBank<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements Bank<G, S, T, H, H0> {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	public ExampleBank(BrandsSchemeSetup<G, S, T, H, H0> setup) {
		this.setup = setup;
		G group = setup.getGroup();
		privateKey = group.getRandomElement(null);
		publicKey = setup.getGenerators()[0].exponentiate(privateKey);
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
		if (obj == null || !(obj instanceof ExampleBank)) {
			return false;
		}
		ExampleBank other = (ExampleBank) obj;
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
