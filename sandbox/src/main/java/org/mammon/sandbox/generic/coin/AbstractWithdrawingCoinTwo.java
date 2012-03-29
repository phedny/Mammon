package org.mammon.sandbox.generic.coin;

import java.lang.reflect.Array;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.IssueCoinsRequest;
import org.mammon.sandbox.messages.IssueCoinsResponse;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExampleGroup.ExampleElement;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractWithdrawingCoinTwo<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		implements Identifiable<I>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;

	private final AccountHolderForBank<G, F, S, T, H, H0> accountHolder;

	private final Bank<G, F, S, T, H, H0> bank;

	private final Element<G> publicKey;

	private final int count;

	private final FiniteField.Element<F>[] blindingFactor;

	private final FiniteField.Element<F>[] payerWitness;

	private final FiniteField.Element<F>[] secondWitness;

	private final Element<G>[] blindedIdentity;

	private final Element<G>[] commitment;

	private final Element<G>[] witnesses;

	private final FiniteField.Element<F>[] challenges;

	public AbstractWithdrawingCoinTwo(
			BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			AccountHolderForBank<G, F, S, T, H, H0> accountHolder,
			Bank<G, F, S, T, H, H0> bank, Element<G> publicKey, int count,
			FiniteField.Element<F>[] blindingFactor,
			FiniteField.Element<F>[] payerWitness,
			FiniteField.Element<F>[] secondWitness,
			Element<G>[] blindedIdentity, Element<G>[] commitment,
			Element<G>[] witnesses, FiniteField.Element<F>[] challenges) {
		this.setup = setup;
		this.accountHolder = accountHolder;
		this.bank = bank;
		this.publicKey = publicKey;
		this.count = count;
		this.blindingFactor = blindingFactor;
		this.payerWitness = payerWitness;
		this.secondWitness = secondWitness;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.witnesses = witnesses;
		this.challenges = challenges;
	}

	@Override
	public Object transition(Message message) {
		if (message instanceof IssueCoinsResponse<?>) {
			IssueCoinsResponse<F> response = (IssueCoinsResponse<F>) message;

			// Tested by account holder
			ExampleGroup.ExampleElement left = (ExampleElement) setup
					.getGenerators()[0].exponentiate(response.getResponse()[0]);
			ExampleGroup.ExampleElement right = (ExampleElement) bank
					.getPublicKey().exponentiate(challenges[0])
					.multiply(witnesses[0]);
			System.out.println(left.simplify() + " <= from: " + left);
			System.out.println(right.simplify() + " <= from: " + right);
			System.out.println(left.simplify().equals(right.simplify()));

			// Tested by account holder
			left = (ExampleElement) accountHolder.getPublicKey()
					.multiply(setup.getGenerators()[2])
					.exponentiate(response.getResponse()[0]);
			right = (ExampleElement) accountHolder.getBlindedIdentity()
					.exponentiate(challenges[0]).multiply(witnesses[1]);
			System.out.println(left.simplify() + " <= from: " + left);
			System.out.println(right.simplify() + " <= from: " + right);
			System.out.println(left.simplify().equals(right.simplify()));

			Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(
					blindingFactor[0]);
			Element<G> a_ = witnesses[0].exponentiate(secondWitness[0])
					.multiply(
							setup.getGenerators()[0]
									.exponentiate(secondWitness[1]));
			Element<G> b_ = witnesses[1].exponentiate(
					blindingFactor[0].multiply(secondWitness[0])).multiply(
					blindedIdentity[0].exponentiate(secondWitness[1]));
			FiniteField.Element<F> r_ = response.getResponse()[0].multiply(
					secondWitness[0]).add(secondWitness[1]);
			Object[] coinSignature = (Object[]) Array.newInstance(Object.class,
					4);
			coinSignature[0] = z_;
			coinSignature[1] = a_;
			coinSignature[2] = b_;
			coinSignature[3] = r_;
			return newUnspentCoin(coinSignature);
		}
		return null;
	}

	@Override
	public Message emitMessage() {
		final int numOfWitnesses = witnesses.length / 2;
		Element<G>[] firstWitnesses = (Element<G>[]) Array.newInstance(
				Element.class, numOfWitnesses);
		for (int i = 0; i < numOfWitnesses; i++) {
			firstWitnesses[i] = witnesses[2 * i];
		}

		return new IssueCoinsRequest<G, F>(new AssetType() {

			@Override
			public String getCallSign() {
				return "EUR";
			}
		}, 1, firstWitnesses, challenges);
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

	protected FiniteField.Element<F>[] getBlindingFactor() {
		return blindingFactor;
	}

	protected FiniteField.Element<F>[] getPayerWitness() {
		return payerWitness;
	}

	protected FiniteField.Element<F>[] getSecondWitness() {
		return secondWitness;
	}

	protected Element<G>[] getBlindedIdentity() {
		return blindedIdentity;
	}

	protected Element<G>[] getCommitment() {
		return commitment;
	}

	protected Element<G>[] getWitnesses() {
		return witnesses;
	}

	protected FiniteField.Element<F>[] getChallenges() {
		return challenges;
	}

	protected abstract ExampleUnspentCoin newUnspentCoin(Object[] coinSignature);

}
