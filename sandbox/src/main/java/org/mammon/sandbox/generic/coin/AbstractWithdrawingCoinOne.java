package org.mammon.sandbox.generic.coin;

import java.lang.reflect.Array;

import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.BankWitnessesRequest;
import org.mammon.sandbox.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractWithdrawingCoinOne<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I>
		implements Identifiable<I>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final AccountHolderForBank<G, S, T, H, H0> accountHolder;

	private final Bank<G, S, T, H, H0> bank;

	private final Element<G> publicKey;

	private final int count;

	protected AbstractWithdrawingCoinOne(AccountHolderForBank<G, S, T, H, H0> accountHolder, Bank<G, S, T, H, H0> bank,
			Element<G> publicKey, int count) {
		this.accountHolder = accountHolder;
		this.bank = bank;
		this.publicKey = publicKey;
		this.count = count;
		this.setup = accountHolder.getSetup();
	}

	@Override
	public Object transition(Message message) {
		if (message instanceof BankWitnessesResponse<?>) {
			BankWitnessesResponse<G> response = (BankWitnessesResponse<G>) message;
			Element<G>[] witnesses = response.getWitness();
			if (witnesses.length % 2 == 0) {
				final int numOfWitnesses = witnesses.length / 2;
				Element<G>[] challenges = (Element<G>[]) Array.newInstance(Element.class, numOfWitnesses);
				Element<G>[] blindingFactor = (Element<G>[]) Array.newInstance(Element.class, numOfWitnesses);
				Element<G>[] payerWitness = (Element<G>[]) Array.newInstance(Element.class, 2 * numOfWitnesses);
				Element<G>[] secondWitness = (Element<G>[]) Array.newInstance(Element.class, 2 * numOfWitnesses);
				Element<G>[] blindedIdentity = (Element<G>[]) Array.newInstance(Element.class, numOfWitnesses);
				Element<G>[] commitment = (Element<G>[]) Array.newInstance(Element.class, numOfWitnesses);

				for (int i = 0; i < numOfWitnesses; i++) {
					Element<G> a = witnesses[2 * i];
					Element<G> b = witnesses[2 * i + 1];

					blindingFactor[i] = setup.getGroup().getRandomElement(null);
					payerWitness[2 * i] = setup.getGroup().getRandomElement(null); // x_1
					payerWitness[2 * i + 1] = setup.getGroup().getRandomElement(null); // x_2
					Element<G> u = setup.getGroup().getRandomElement(null);
					Element<G> v = setup.getGroup().getRandomElement(null);

					blindedIdentity[i] = accountHolder.getPublicKey().multiply(setup.getGenerators()[2]).exponentiate(
							blindingFactor[i]); // A
					Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(blindingFactor[i]);
					commitment[i] = setup.getGenerators()[1].exponentiate(payerWitness[0]).multiply(
							setup.getGenerators()[2].exponentiate(payerWitness[1])); // B

					Element<G> a_ = a.exponentiate(u).multiply(setup.getGenerators()[0].exponentiate(v));
					Element<G> b_ = b.exponentiate(blindingFactor[i].multiply(u)).multiply(
							blindedIdentity[i].exponentiate(v));

					Element<G> c_ = setup.getSignatureHash().hash(blindedIdentity[i], commitment[i], z_, a_, b_);
					Element<G> c = c_.multiply(u.getInverse());
					challenges[i] = c;
					secondWitness[2 * i] = u;
					secondWitness[2 * i + 1] = v;
				}
				return newWithdrawingCoinTwo(witnesses, challenges, blindingFactor, payerWitness, secondWitness,
						blindedIdentity, commitment);
			}
		}
		return null;
	}

	protected abstract AbstractWithdrawingCoinTwo<G, S, T, H, H0, I> newWithdrawingCoinTwo(Element<G>[] witnesses, Element<G>[] challenges,
			Element<G>[] blindingFactor, Element<G>[] payerWitness, Element<G>[] secondWitness,
			Element<G>[] blindedIdentity, Element<G>[] commitment);

	@Override
	public Message emitMessage() {
		return new BankWitnessesRequest<G, String>(((Identifiable<String>) bank).getIdentity(), publicKey, count);
	}

	protected BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
		return setup;
	}

	protected AccountHolderForBank<G, S, T, H, H0> getAccountHolder() {
		return accountHolder;
	}

	protected Bank<G, S, T, H, H0> getBank() {
		return bank;
	}

	protected Element<G> getPublicKey() {
		return publicKey;
	}

	protected int getCount() {
		return count;
	}

}
