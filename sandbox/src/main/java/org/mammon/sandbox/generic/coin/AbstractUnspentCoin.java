package org.mammon.sandbox.generic.coin;

import java.util.Arrays;

import org.mammon.AssetType;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolder;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.UnspentCoin;

public abstract class AbstractUnspentCoin<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I>
		implements UnspentCoin<G, S, T, H, H0> {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final AccountHolderPrivate<G, S, T, H, H0> bearer;

	private final Bank<G, S, T, H, H0> bank;

	private final Element<G> blindingFactor;

	private final Element<G>[] payerWitness;

	private final Element<G> blindedIdentity;

	private final Element<G> commitment;

	private final Element<G>[] coinSignature;

	protected AbstractUnspentCoin(BrandsSchemeSetup<G, S, T, H, H0> setup, AccountHolderPrivate<G, S, T, H, H0> bearer,
			Bank<G, S, T, H, H0> bank, Element<G> blindingFactor, Element<G>[] payerWitness,
			Element<G> blindedIdentity, Element<G> commitment, Element<G>[] coinSignature) {
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
	public AccountHolder<G, S, T, H, H0> getBearer() {
		return bearer;
	}

	@Override
	public Element<G> getBlindingFactor() {
		return blindingFactor;
	}

	@Override
	public Element<G>[] getPayerWitness() {
		return payerWitness.clone();
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public Element<G>[] getCoinSignature() {
		return coinSignature;
	}

	@Override
	public Element<G> getCommitment() {
		return commitment;
	}

	@Override
	public Bank<G, S, T, H, H0> getIssuer() {
		return bank;
	}

	@Override
	public BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
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
		if (obj == null || !(obj instanceof AbstractUnspentCoin<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractUnspentCoin<?, ?, ?, ?, ?, ?> other = (AbstractUnspentCoin<?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && bank.equals(other.bank) && bearer.equals(other.bearer)
				&& blindingFactor.equals(other.blindingFactor) && Arrays.deepEquals(payerWitness, other.payerWitness);
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
