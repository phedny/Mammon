package org.mammon.sandbox.generic.coin;

import java.util.Arrays;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolder;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.UnspentCoin;

public abstract class AbstractUnspentCoin<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		implements UnspentCoin<G, F, S, T, H, H0> {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;

	private final AccountHolderPrivate<G, F, S, T, H, H0> bearer;

	private final Bank<G, F, S, T, H, H0> bank;

	private final FiniteField.Element<F> blindingFactor;

	private final FiniteField.Element<F>[] payerWitness;

	private final Element<G> blindedIdentity;

	private final Element<G> commitment;

	private final Object[] coinSignature;

	protected AbstractUnspentCoin(BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			AccountHolderPrivate<G, F, S, T, H, H0> bearer,
			Bank<G, F, S, T, H, H0> bank,
			FiniteField.Element<F> blindingFactor,
			FiniteField.Element<F>[] payerWitness, Element<G> blindedIdentity,
			Element<G> commitment, Object[] coinSignature) {
		this.setup = setup;
		this.bearer = bearer;
		this.bank = bank;
		this.blindingFactor = blindingFactor;
		this.payerWitness = payerWitness;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.coinSignature = coinSignature;
	}

	@Override
	public AccountHolder<G, F, S, T, H, H0> getBearer() {
		return bearer;
	}

	@Override
	public FiniteField.Element<F> getBlindingFactor() {
		return blindingFactor;
	}

	@Override
	public FiniteField.Element<F>[] getPayerWitness() {
		return payerWitness.clone();
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public Object[] getCoinSignature() {
		return coinSignature;
	}

	@Override
	public Element<G> getCommitment() {
		return commitment;
	}

	@Override
	public Bank<G, F, S, T, H, H0> getIssuer() {
		return bank;
	}

	@Override
	public BrandsSchemeSetup<G, F, S, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public AssetType getAssetType() {
		return new AssetType() {

			@Override
			public String getCallSign() {
				return "EUR";
			}
		};
	}

	@Override
	public Number getFaceValue() {
		return Integer.valueOf(1);
	}

	@Override
	public boolean isSellable() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null
				|| !(obj instanceof AbstractUnspentCoin<?, ?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractUnspentCoin<?, ?, ?, ?, ?, ?, ?> other = (AbstractUnspentCoin<?, ?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && bank.equals(other.bank)
				&& bearer.equals(other.bearer)
				&& blindingFactor.equals(other.blindingFactor)
				&& Arrays.deepEquals(payerWitness, other.payerWitness);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, bank);
		hashCode = HashCodeUtil.hash(hashCode, bearer);
		hashCode = HashCodeUtil.hash(hashCode, blindingFactor);
		hashCode = HashCodeUtil.hash(hashCode, payerWitness);
		return hashCode;
	}

}
