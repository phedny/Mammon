package org.mammon.scheme.brands.generic.coin;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.messages.IssueCoinsRequest;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractWithdrawingCoinTwo<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransitionable<I> implements Identifiable<I>, Transitionable<I>, MessageEmitter {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final AccountHolderForBank<G, F, I, T, H, H0> accountHolder;

	private final Bank<G, F, I, T, H, H0> bank;

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

	public AbstractWithdrawingCoinTwo(BrandsSchemeSetup<G, F, I, T, H, H0> setup,
			AccountHolderForBank<G, F, I, T, H, H0> accountHolder, Bank<G, F, I, T, H, H0> bank,
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
		FiniteField.Element<F> r = response.getResponse();
		Group.Element<G> left = setup.getGenerators()[0].exponentiate(r);
		Group.Element<G> right = bank.getPublicKey().exponentiate(c).multiply(a);
		System.out.println(left.equals(right));

		// Tested by account holder
		left = accountHolder.getPublicKey().multiply(setup.getGenerators()[2]).exponentiate(r);
		right = accountHolder.getBlindedIdentity().exponentiate(c).multiply(b);
		System.out.println(left.equals(right));

		Group.Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(s);
		Group.Element<G> a_ = a.exponentiate(u).multiply(setup.getGenerators()[0].exponentiate(v));
		Group.Element<G> b_ = b.exponentiate(s.multiply(u)).multiply(bigA.exponentiate(v));
		FiniteField.Element<F> r_ = r.multiply(u).add(v);
		return newUnspentCoin(r, newCoinSignature(z_, a_, b_, r_));
	}

	public AbstractWithdrawingCoinTwo<G, F, I, T, H, H0> transition(BankWitnessesResponse<G> response) {
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
		}, Integer.valueOf(1), a, c);
	}

	protected BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	protected AccountHolderForBank<G, F, I, T, H, H0> getAccountHolder() {
		return accountHolder;
	}

	protected Bank<G, F, I, T, H, H0> getBank() {
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

	protected abstract AbstractUnspentCoin<G, F, I, T, H, H0> newUnspentCoin(FiniteField.Element<F> r, CoinSignature<G, F> coinSignature);

	protected abstract AbstractCoinSignature<G, F> newCoinSignature(Group.Element<G> z, Group.Element<G> a,
			Group.Element<G> b, FiniteField.Element<F> r);

}
