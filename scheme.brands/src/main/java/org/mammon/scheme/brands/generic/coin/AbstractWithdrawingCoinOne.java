package org.mammon.scheme.brands.generic.coin;

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
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.messages.BankWitnessesRequest;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractWithdrawingCoinOne<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransitionable<I> implements Identifiable<I>, Transitionable<I>, MessageEmitter {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final AccountHolderForBank<G, F, I, T, H, H0> accountHolder;

	private final AbstractBank<G, F, I, T, H, H0> bank;

	private final Group.Element<G> publicKey;

	private final int count;

	protected AbstractWithdrawingCoinOne(AccountHolderForBank<G, F, I, T, H, H0> accountHolder,
			AbstractBank<G, F, I, T, H, H0> bank, Group.Element<G> publicKey, int count) {
		this.accountHolder = accountHolder;
		this.bank = bank;
		this.publicKey = publicKey;
		this.count = count;
		this.setup = accountHolder.getSetup();
	}

	public AbstractWithdrawingCoinTwo<G, F, I, T, H, H0> transition(BankWitnessesResponse<G> response) {
		Group.Element<G> a = response.getValA();
		Group.Element<G> b = response.getValB();

		FiniteField.Element<F> s = setup.getFiniteField().getRandomElement();
		FiniteField.Element<F> x1 = setup.getFiniteField().getRandomElement();
		FiniteField.Element<F> x2 = setup.getFiniteField().getRandomElement();
		FiniteField.Element<F> u = setup.getFiniteField().getRandomElement();
		FiniteField.Element<F> v = setup.getFiniteField().getRandomElement();

		Group.Element<G> bigA = accountHolder.getPublicKey().multiply(setup.getGenerator(2)).exponentiate(s);
		Group.Element<G> z_ = accountHolder.getBlindedIdentity().exponentiate(s);
		Group.Element<G> bigB = setup.getGenerator(1).exponentiate(x1).multiply(
				setup.getGenerator(2).exponentiate(x2));

		Group.Element<G> a_ = a.exponentiate(u).multiply(setup.getGenerator(0).exponentiate(v));
		Group.Element<G> b_ = b.exponentiate(s.multiply(u)).multiply(bigA.exponentiate(v));

		FiniteField.Element<F> c_ = setup.getSignatureHash().hash(bigA, bigB, z_, a_, b_);
		FiniteField.Element<F> c = c_.multiply(u.getInverse());
		return newWithdrawingCoinTwo(a, b, c, s, x1, x2, u, v, bigA, bigB);
	}

	protected abstract AbstractWithdrawingCoinTwo<G, F, I, T, H, H0> newWithdrawingCoinTwo(Group.Element<G> a,
			Group.Element<G> b, FiniteField.Element<F> c, FiniteField.Element<F> s, FiniteField.Element<F> x1,
			FiniteField.Element<F> x2, FiniteField.Element<F> u, FiniteField.Element<F> v, Group.Element<G> bigA,
			Group.Element<G> bigB);

	@Override
	public Message emitMessage() {
		return new BankWitnessesRequest<G, I>(bank.getIdentity(), publicKey, count);
	}

	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	public AccountHolderForBank<G, F, I, T, H, H0> getAccountHolder() {
		return accountHolder;
	}

	public Bank<G, F, I, T, H, H0> getBank() {
		return bank;
	}

	public Group.Element<G> getPublicKey() {
		return publicKey;
	}

	public int getCount() {
		return count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountHolder == null) ? 0 : accountHolder.hashCode());
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + count;
		result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
		result = prime * result + ((setup == null) ? 0 : setup.hashCode());
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
		AbstractWithdrawingCoinOne other = (AbstractWithdrawingCoinOne) obj;
		if (accountHolder == null) {
			if (other.accountHolder != null)
				return false;
		} else if (!accountHolder.equals(other.accountHolder))
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (count != other.count)
			return false;
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		if (setup == null) {
			if (other.setup != null)
				return false;
		} else if (!setup.equals(other.setup))
			return false;
		return true;
	}

}
