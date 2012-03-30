package org.mammon.sandbox.generic.coin;

import java.lang.reflect.Array;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.BankWitnessesRequest;
import org.mammon.sandbox.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractWithdrawingCoinOne<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		implements Identifiable<I>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;

	private final AccountHolderForBank<G, F, S, T, H, H0> accountHolder;

	private final Bank<G, F, S, T, H, H0> bank;

	private final Group.Element<G> publicKey;

	private final int count;

	protected AbstractWithdrawingCoinOne(
			AccountHolderForBank<G, F, S, T, H, H0> accountHolder,
			Bank<G, F, S, T, H, H0> bank, Group.Element<G> publicKey, int count) {
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
				FiniteField.Element<F>[] challenges = (FiniteField.Element<F>[]) Array
						.newInstance(FiniteField.Element.class, numOfWitnesses);
				FiniteField.Element<F>[] blindingFactor = (FiniteField.Element<F>[]) Array
						.newInstance(FiniteField.Element.class, numOfWitnesses);
				FiniteField.Element<F>[] payerWitness = (FiniteField.Element<F>[]) Array
						.newInstance(FiniteField.Element.class,
								2 * numOfWitnesses);
				FiniteField.Element<F>[] secondWitness = (FiniteField.Element<F>[]) Array
						.newInstance(FiniteField.Element.class,
								2 * numOfWitnesses);
				Element<G>[] blindedIdentity = (Element<G>[]) Array
						.newInstance(Element.class, numOfWitnesses);
				Element<G>[] commitment = (Element<G>[]) Array.newInstance(
						Element.class, numOfWitnesses);

				for (int i = 0; i < numOfWitnesses; i++) {
					Element<G> a = witnesses[2 * i];
					Element<G> b = witnesses[2 * i + 1];

					blindingFactor[i] = setup.getFiniteField().getRandomElement();
					payerWitness[2 * i] = setup.getFiniteField().getRandomElement(); // x_1
					payerWitness[2 * i + 1] = setup.getFiniteField().getRandomElement(); // x_2
					FiniteField.Element<F> u = setup.getFiniteField().getRandomElement();
					FiniteField.Element<F> v = setup.getFiniteField().getRandomElement();

					blindedIdentity[i] = accountHolder.getPublicKey()
							.multiply(setup.getGenerators()[2])
							.exponentiate(blindingFactor[i]); // A
					Element<G> z_ = accountHolder.getBlindedIdentity()
							.exponentiate(blindingFactor[i]);
					commitment[i] = setup.getGenerators()[1].exponentiate(
							payerWitness[0]).multiply(
							setup.getGenerators()[2]
									.exponentiate(payerWitness[1])); // B

					Element<G> a_ = a.exponentiate(u).multiply(
							setup.getGenerators()[0].exponentiate(v));
					Element<G> b_ = b.exponentiate(
							blindingFactor[i].multiply(u)).multiply(
							blindedIdentity[i].exponentiate(v));

					FiniteField.Element<F> c_ = setup.getSignatureHash().hash(
							blindedIdentity[i], commitment[i], z_, a_, b_);
					FiniteField.Element<F> c = c_.multiply(u.getInverse());
					challenges[i] = c;
					secondWitness[2 * i] = u;
					secondWitness[2 * i + 1] = v;
				}
				return newWithdrawingCoinTwo(witnesses, challenges,
						blindingFactor, payerWitness, secondWitness,
						blindedIdentity, commitment);
			}
		}
		return null;
	}

	protected abstract AbstractWithdrawingCoinTwo<G, F, S, T, H, H0, I> newWithdrawingCoinTwo(
			Element<G>[] witnesses, FiniteField.Element<F>[] challenges,
			FiniteField.Element<F>[] blindingFactor,
			FiniteField.Element<F>[] payerWitness,
			FiniteField.Element<F>[] secondWitness,
			Element<G>[] blindedIdentity, Element<G>[] commitment);

	@Override
	public Message emitMessage() {
		return new BankWitnessesRequest<G, String>(
				((Identifiable<String>) bank).getIdentity(), publicKey, count);
	}

	protected BrandsSchemeSetup<G, F, S, T, H, H0> getSetup() {
		return setup;
	}

	protected AccountHolderForBank<G, F, S, T, H, H0> getAccountHolder() {
		return accountHolder;
	}

	protected Bank<G, F, S, T, H, H0> getBank() {
		return bank;
	}

	protected Element<G> getPublicKey() {
		return publicKey;
	}

	protected int getCount() {
		return count;
	}

}
