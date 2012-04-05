package org.mammon.scheme.brands.generic.coin;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transitionable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolder;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.util.HashCodeUtil;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractUnspentCoin<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractCoin<G, F, I, T, H, H0> implements UnspentCoin<G, F, I, T, H, H0>, Identifiable<I>,
		Transitionable<I>, DualIdentityTransitionable<I> {

	private final AccountHolderPrivate<G, F, I, T, H, H0> bearer;

	private final I dualIdentity;

	private final FiniteField.Element<F> s;

	private final FiniteField.Element<F> x1;

	private final FiniteField.Element<F> x2;

	private final FiniteField.Element<F> r;

	protected AbstractUnspentCoin(BrandsSchemeSetup<G, F, I, T, H, H0> setup,
			AccountHolderPrivate<G, F, I, T, H, H0> bearer, Bank<G, F, I, T, H, H0> bank, I identity, I dualIdentity,
			FiniteField.Element<F> blindingFactor, FiniteField.Element<F> x1, FiniteField.Element<F> x2,
			Group.Element<G> blindedIdentity, Group.Element<G> commitment, FiniteField.Element<F> r,
			CoinSignature<G, F> coinSignature) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, new AssetType() {

			@Override
			public String getCallSign() {
				return "EUR";
			}
		}, Integer.valueOf(1), identity);
		this.bearer = bearer;
		this.dualIdentity = dualIdentity;
		this.s = blindingFactor;
		this.x1 = x1;
		this.x2 = x2;
		this.r = r;
	}

	@Override
	public AccountHolder<G, F, I, T, H, H0> getBearer() {
		return bearer;
	}

	@Override
	public FiniteField.Element<F> getBlindingFactor() {
		return s;
	}

	@Override
	public FiniteField.Element<F> getPayerWitness1() {
		return x1;
	}

	@Override
	public FiniteField.Element<F> getPayerWitness2() {
		return x2;
	}
	
	public FiniteField.Element<F> getR() {
		return r;
	}

	@Override
	public boolean isSellable() {
		return true;
	}

	@Override
	public Transitionable<I> getSecondaryTransitionable() {
		return new SecondaryTransitionable();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractUnspentCoin<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractUnspentCoin<?, ?, ?, ?, ?, ?> other = (AbstractUnspentCoin<?, ?, ?, ?, ?, ?>) obj;
		return getSetup().equals(other.getSetup()) && getIssuer().equals(other.getIssuer()) && bearer.equals(other.bearer) && s.equals(other.s)
				&& x1.equals(other.x1) && x2.equals(other.x2);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, getSetup());
		hashCode = HashCodeUtil.hash(hashCode, getIssuer());
		hashCode = HashCodeUtil.hash(hashCode, bearer);
		hashCode = HashCodeUtil.hash(hashCode, s);
		hashCode = HashCodeUtil.hash(hashCode, x1);
		hashCode = HashCodeUtil.hash(hashCode, x2);
		return hashCode;
	}

	protected final class SecondaryTransitionable extends AbstractTransitionable<I> {
		@Override
		public I getIdentity() {
			return dualIdentity;
		}

		public Object transition(IssueCoinsResponse<F> response) {
			if (r.equals(response.getResponse())) {
				return AbstractUnspentCoin.this;
			} else {
				return null;
			}
		}
	}

}
