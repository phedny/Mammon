package org.mammon.scheme.brands.generic.coin;

import java.util.logging.Logger;

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
import org.mammon.scheme.brands.generic.assettypes.EuroAssetType;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.messages.IssueCoinsRequest;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractWithdrawingCoinTwo<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractTransitionable implements Identifiable, Transitionable, MessageEmitter {
	
	private static final Logger LOG = Logger.getLogger(AbstractWithdrawingCoinTwo.class.getName());

	private final BrandsSchemeSetup<G, F, T, H, H0> setup;

	private final AccountHolderForBank<G, F, T, H, H0> accountHolder;

	private final Bank<G, F, T, H, H0> bank;

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

	protected final String identity;

	public AbstractWithdrawingCoinTwo(BrandsSchemeSetup<G, F, T, H, H0> setup,
			AccountHolderForBank<G, F, T, H, H0> accountHolder, String identity, Bank<G, F, T, H, H0> bank,
			Group.Element<G> publicKey, int count, FiniteField.Element<F> s, FiniteField.Element<F> x1,
			FiniteField.Element<F> x2, FiniteField.Element<F> u, FiniteField.Element<F> v, Group.Element<G> bigA,
			Group.Element<G> bigB, Group.Element<G> a, Group.Element<G> b, FiniteField.Element<F> c) {
		this.setup = setup;
		this.accountHolder = accountHolder;
		this.identity = identity;
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
		Group.Element<G> left = setup.getGenerator(0).exponentiate(r);
		Group.Element<G> right = bank.getPublicKey().exponentiate(c).multiply(a);
		LOG.fine("left == right? " + left.equals(right));

		// Tested by account holder
		left = accountHolder.getPublicKey().multiply(setup.getGenerator(2)).exponentiate(r);
		right = accountHolder.getBlindedIdentity().exponentiate(c).multiply(b);
		LOG.fine("left == right? " + left.equals(right));

		Group.Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(s);
		Group.Element<G> a_ = a.exponentiate(u).multiply(setup.getGenerator(0).exponentiate(v));
		Group.Element<G> b_ = b.exponentiate(s.multiply(u)).multiply(bigA.exponentiate(v));
		FiniteField.Element<F> r_ = r.multiply(u).add(v);
		return newUnspentCoin(r, newCoinSignature(z_, a_, b_, r_));
	}

	public AbstractWithdrawingCoinTwo<G, F, T, H, H0> transition(BankWitnessesResponse<G> response) {
		if (a.equals(response.getValA()) && b.equals(response.getValB())) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public Message emitMessage() {
		return new IssueCoinsRequest<G, F>(new EuroAssetType(), Integer.valueOf(1), c);
	}

	public BrandsSchemeSetup<G, F, T, H, H0> getSetup() {
		return setup;
	}

	public AccountHolderForBank<G, F, T, H, H0> getAccountHolder() {
		return accountHolder;
	}

	public Bank<G, F, T, H, H0> getBank() {
		return bank;
	}

	public Group.Element<G> getPublicKey() {
		return publicKey;
	}

	public int getCount() {
		return count;
	}

	public FiniteField.Element<F> getBlindingFactor() {
		return s;
	}

	public FiniteField.Element<F> getX1() {
		return x1;
	}

	public FiniteField.Element<F> getX2() {
		return x2;
	}

	public FiniteField.Element<F> getU() {
		return u;
	}

	public FiniteField.Element<F> getV() {
		return v;
	}

	public Group.Element<G> getBigA() {
		return bigA;
	}

	public Group.Element<G> getBigB() {
		return bigB;
	}

	public Group.Element<G> getA() {
		return a;
	}

	public Group.Element<G> getB() {
		return b;
	}

	public FiniteField.Element<F> getC() {
		return c;
	}

	protected abstract AbstractUnspentCoin<G, F, T, H, H0> newUnspentCoin(FiniteField.Element<F> r,
			CoinSignature<G, F> coinSignature);

	protected abstract AbstractCoinSignature<G, F> newCoinSignature(Group.Element<G> z, Group.Element<G> a,
			Group.Element<G> b, FiniteField.Element<F> r);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((accountHolder == null) ? 0 : accountHolder.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((bigA == null) ? 0 : bigA.hashCode());
		result = prime * result + ((bigB == null) ? 0 : bigB.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + count;
		result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + ((setup == null) ? 0 : setup.hashCode());
		result = prime * result + ((u == null) ? 0 : u.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		result = prime * result + ((x1 == null) ? 0 : x1.hashCode());
		result = prime * result + ((x2 == null) ? 0 : x2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractWithdrawingCoinTwo other = (AbstractWithdrawingCoinTwo) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (accountHolder == null) {
			if (other.accountHolder != null)
				return false;
		} else if (!accountHolder.equals(other.accountHolder))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (bigA == null) {
			if (other.bigA != null)
				return false;
		} else if (!bigA.equals(other.bigA))
			return false;
		if (bigB == null) {
			if (other.bigB != null)
				return false;
		} else if (!bigB.equals(other.bigB))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (count != other.count)
			return false;
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		if (setup == null) {
			if (other.setup != null)
				return false;
		} else if (!setup.equals(other.setup))
			return false;
		if (u == null) {
			if (other.u != null)
				return false;
		} else if (!u.equals(other.u))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		if (x1 == null) {
			if (other.x1 != null)
				return false;
		} else if (!x1.equals(other.x1))
			return false;
		if (x2 == null) {
			if (other.x2 != null)
				return false;
		} else if (!x2.equals(other.x2))
			return false;
		return true;
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

}
