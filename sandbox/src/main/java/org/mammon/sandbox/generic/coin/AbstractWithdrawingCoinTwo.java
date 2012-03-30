package org.mammon.sandbox.generic.coin;

import java.lang.reflect.Array;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.generic.messaging.AbstractTransitionable;
import org.mammon.sandbox.messages.BankWitnessesResponse;
import org.mammon.sandbox.messages.IssueCoinsRequest;
import org.mammon.sandbox.messages.IssueCoinsResponse;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.sandbox.objects.example.ExampleGroup.ExampleElement;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractWithdrawingCoinTwo<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		extends AbstractTransitionable<I> implements Identifiable<I>, Transitionable<I>, MessageEmitter {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;

	private final AccountHolderForBank<G, F, S, T, H, H0> accountHolder;

	private final Bank<G, F, S, T, H, H0> bank;

	private final Group.Element<G> publicKey;

	private final int count;

	private final FiniteField.Element<F> s;

	private final FiniteField.Element<F> x1;

	private final FiniteField.Element<F> x2;

	private final FiniteField.Element<F> u;

	private final FiniteField.Element<F> v;

	private final Group.Element<G> bigA;

	private final Group.Element<G> bigB;

	private final Group.Element<G> a;

	private final Group.Element<G> b;

	private final FiniteField.Element<F> c;

	public AbstractWithdrawingCoinTwo(BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			AccountHolderForBank<G, F, S, T, H, H0> accountHolder, Bank<G, F, S, T, H, H0> bank,
			Group.Element<G> publicKey, int count, FiniteField.Element<F> s, FiniteField.Element<F> x1,
			FiniteField.Element<F> x2, FiniteField.Element<F> u, FiniteField.Element<F> v, Group.Element<G> bigA,
			Group.Element<G> bigB, Group.Element<G> a, Group.Element<G> b, FiniteField.Element<F> c) {
		this.setup = setup;
		this.accountHolder = accountHolder;
		this.bank = bank;
		this.publicKey = publicKey;
		this.count = count;
		this.s = s;
		this.x1 = x1;
		this.x2 = x2;
		this.u = u;
		this.v = v;
		this.bigA = bigA;
		this.bigB = bigB;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public Object transition(IssueCoinsResponse<F> response) {

		// Tested by account holder
		ExampleGroup.ExampleElement left = (ExampleElement) setup.getGenerators()[0].exponentiate(response
				.getResponse());
		ExampleGroup.ExampleElement right = (ExampleElement) bank.getPublicKey().exponentiate(c).multiply(a);
		System.out.println(left.simplify() + " <= from: " + left);
		System.out.println(right.simplify() + " <= from: " + right);
		System.out.println(left.simplify().equals(right.simplify()));

		// Tested by account holder
		left = (ExampleElement) accountHolder.getPublicKey().multiply(setup.getGenerators()[2]).exponentiate(
				response.getResponse());
		right = (ExampleElement) accountHolder.getBlindedIdentity().exponentiate(c).multiply(b);
		System.out.println(left.simplify() + " <= from: " + left);
		System.out.println(right.simplify() + " <= from: " + right);
		System.out.println(left.simplify().equals(right.simplify()));

		Group.Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(s);
		Group.Element<G> a_ = a.exponentiate(u).multiply(setup.getGenerators()[0].exponentiate(v));
		Group.Element<G> b_ = b.exponentiate(s.multiply(u)).multiply(bigA.exponentiate(v));
		FiniteField.Element<F> r_ = response.getResponse().multiply(u).add(v);
		Object[] coinSignature = (Object[]) Array.newInstance(Object.class, 4);
		coinSignature[0] = z_;
		coinSignature[1] = a_;
		coinSignature[2] = b_;
		coinSignature[3] = r_;
		return newUnspentCoin(coinSignature);
	}
	
	public AbstractWithdrawingCoinTwo<G, F, S, T, H, H0, I> transition(BankWitnessesResponse<G> response) {
		if (a.equals(response.getValA()) && b.equals(response.getValB())) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public Message emitMessage() {
		return new IssueCoinsRequest<G, F>(new AssetType() {

			@Override
			public String getCallSign() {
				return "EUR";
			}
		}, 1, a, c);
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

	protected Group.Element<G> getPublicKey() {
		return publicKey;
	}

	protected int getCount() {
		return count;
	}

	protected FiniteField.Element<F> getBlindingFactor() {
		return s;
	}

	protected FiniteField.Element<F> getX1() {
		return x1;
	}

	protected FiniteField.Element<F> getX2() {
		return x2;
	}

	protected FiniteField.Element<F> getU() {
		return u;
	}

	protected FiniteField.Element<F> getV() {
		return v;
	}

	protected Group.Element<G> getBigA() {
		return bigA;
	}

	protected Group.Element<G> getBigB() {
		return bigB;
	}

	protected Group.Element<G> getA() {
		return a;
	}
	
	protected Group.Element<G> getB() {
		return b;
	}

	protected FiniteField.Element<F> getC() {
		return c;
	}

	protected abstract ExampleUnspentCoin newUnspentCoin(Object[] coinSignature);

}
