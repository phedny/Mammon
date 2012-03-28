package org.mammon.sandbox.objects.accountholder;

import java.lang.reflect.Array;
import java.util.UUID;

import org.mammon.AssetType;
import org.mammon.brands.AccountHolder;
import org.mammon.brands.Bank;
import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group;
import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.IssueCoinsRequest;
import org.mammon.sandbox.messages.IssueCoinsResponse;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.sandbox.objects.example.ExampleGroup.ExampleElement;

public class WithdrawingCoinTwo<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements Identifiable<String>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final AccountHolder<G, S, T, H, H0> accountHolder;

	private final UUID identity;

	private final Bank bank;

	private final Element<G> publicKey;

	private final int count;

	private final Element<G>[] blindingFactor;

	private final Element<G>[] payerWitness;

	private final Element<G>[] secondWitness;

	private final Element<G>[] blindedIdentity;

	private final Element<G>[] commitment;

	private final Element<G>[] witnesses;

	private final Element<G>[] challenges;

	public WithdrawingCoinTwo(BrandsSchemeSetup<G, S, T, H, H0> setup, AccountHolder<G, S, T, H, H0> accountHolder,
			UUID identity, Bank bank, Element<G> publicKey, int count, Element<G>[] blindingFactor,
			Element<G>[] payerWitness, Element<G>[] secondWitness, Element<G>[] blindedIdentity,
			Element<G>[] commitment, Element<G>[] witnesses, Element<G>[] challenges) {
		this.setup = setup;
		this.accountHolder = accountHolder;
		this.identity = identity;
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
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	public Object transition(Message message) {
		if (message instanceof IssueCoinsResponse<?>) {
			IssueCoinsResponse<G> response = (IssueCoinsResponse<G>) message;

			// Tested by account holder
			ExampleElement left = (ExampleElement) setup.getGenerators()[0].exponentiate(response.getResponse()[0]);
			ExampleElement right = (ExampleElement) bank.getPublicKey().exponentiate(challenges[0]).multiply(
					witnesses[0]);
			System.out.println(left.simplify() + " <= from: " + left);
			System.out.println(right.simplify() + " <= from: " + right);
			System.out.println(left.simplify().equals(right.simplify()));

			// Tested by account holder
			left = (ExampleElement) accountHolder.getPublicKey().multiply(setup.getGenerators()[2]).exponentiate(
					response.getResponse()[0]);
			right = (ExampleElement) accountHolder.getBlindedIdentity().exponentiate(challenges[0]).multiply(
					witnesses[1]);
			System.out.println(left.simplify() + " <= from: " + left);
			System.out.println(right.simplify() + " <= from: " + right);
			System.out.println(left.simplify().equals(right.simplify()));

			Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(blindingFactor[0]);
			Element<G> a_ = payerWitness[0].exponentiate(secondWitness[0]).multiply(
					setup.getGenerators()[0].exponentiate(secondWitness[1]));
			Element<G> b_ = payerWitness[1].exponentiate(blindingFactor[0].multiply(secondWitness[0])).multiply(
					blindedIdentity[0].exponentiate(secondWitness[1]));
			Element<G> r_ = response.getResponse()[0].multiply(secondWitness[0]).add(secondWitness[1]);
			Element<G>[] coinSignature = (Element<G>[]) Array.newInstance(Element.class, 4);
			coinSignature[0] = z_;
			coinSignature[1] = a_;
			coinSignature[2] = b_;
			coinSignature[3] = r_;
			return new ExampleUnspentCoin<G, S, T, H, H0>((BrandsSchemeSetup<G, S, T, H, H0>) setup,
					(ExampleAccountHolder<G, S, T, H, H0>) accountHolder, (ExampleBank<G, S, T, H, H0>) bank,
					blindingFactor[0], payerWitness, blindedIdentity[0], commitment[0], coinSignature);
		}
		return null;
	}

	@Override
	public Message emitMessage() {
		final int numOfWitnesses = witnesses.length / 2;
		Element<G>[] firstWitnesses = (Element<G>[]) Array.newInstance(Element.class, numOfWitnesses);
		for (int i = 0; i < numOfWitnesses; i++) {
			firstWitnesses[i] = witnesses[2 * i];
		}

		return new IssueCoinsRequest<G>(new AssetType() {

			@Override
			public String getCallSign() {
				return "EUR";
			}
		}, 1, firstWitnesses, challenges);
	}
}
