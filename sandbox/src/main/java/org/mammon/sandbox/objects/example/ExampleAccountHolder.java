package org.mammon.sandbox.objects.example;

import org.mammon.brands.AccountHolder;
import org.mammon.brands.Bank;
import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group;
import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;

public class ExampleAccountHolder<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements AccountHolder<G, S, T, H, H0>, Identifiable<String>, Transactable {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	private final Element<G> blindedIdentity;

	private final Bank bank;

	public ExampleAccountHolder(BrandsSchemeSetup<G, S, T, H, H0> setup, Element<G> privateKey, Element<G> publicKey,
			Element<G> blindedIdentity, Bank bank) {
		this.setup = setup;
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.blindedIdentity = blindedIdentity;
		this.bank = bank;
	}

	public ExampleAccountHolder(BrandsSchemeSetup<G, S, T, H, H0> setup, ExampleBank<G, S, T, H, H0> bank) {
		this.setup = setup;
		privateKey = setup.getGroup().getRandomElement(null);
		publicKey = setup.getGenerators()[1].exponentiate(privateKey);
		blindedIdentity = publicKey.multiply(setup.getGenerators()[2]).exponentiate(bank.getPrivateKey());
		this.bank = bank;
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
		if (obj == null || !(obj instanceof ExampleAccountHolder<?, ?, ?, ?, ?>)) {
			return false;
		}
		ExampleAccountHolder<?, ?, ?, ?, ?> other = (ExampleAccountHolder<?, ?, ?, ?, ?>) obj;
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

	@Override
	public String getIdentity() {
		return "accountholder-" + blindedIdentity;
	}

	@Override
	public Object transact(Message message) {
		if (message instanceof ObtainCoinsMessage<?>) {
			ObtainCoinsMessage<String> request = (ObtainCoinsMessage<String>) message;
			return new WithdrawingCoinOne<G, S, T, H, H0>(this, bank, publicKey, request.getCount());
		}
		return null;
	}

}
