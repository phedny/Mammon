package org.mammon.scheme.brands.generic.coin;

import java.util.logging.Logger;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.coin.SpentCoin;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.shop.AbstractShop;
import org.mammon.scheme.brands.messages.CoinTransferMessage;
import org.mammon.scheme.brands.shop.Shop;
import org.mammon.util.HashCodeUtil;

public abstract class AbstractSpentCoin<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractCoin<G, F, I, T, H, H0> implements SpentCoin<G, F, I, T, H, H0> {
	
	private static final Logger LOG = Logger.getLogger(AbstractSpentCoin.class.getName());

	private final Shop<G, F, I, T, H, H0> bearer;

	private final T time;

	private final FiniteField.Element<F> r1;

	private final FiniteField.Element<F> r2;

	public AbstractSpentCoin(BrandsSchemeSetup<G, F, I, T, H, H0> setup, AbstractBank<G, F, I, T, H, H0> bank,
			Group.Element<G> blindedIdentity, Group.Element<G> commitment, CoinSignature<G, F> coinSignature,
			AssetType assetType, Number faceValue, String identity, Shop<G, F, I, T, H, H0> bearer, T time,
			FiniteField.Element<F> r1, FiniteField.Element<F> r2) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, assetType, faceValue, identity);
		this.bearer = bearer;
		this.time = time;
		this.r1 = r1;
		this.r2 = r2;
	}

	@SuppressWarnings("unchecked")
	public AbstractSpentCoin(UnspentCoin<G, F, I, T, H, H0> basedOnCoin, Bank<G, F, I, T, H, H0> bank,
			AbstractShop<G, F, I, T, H, H0> bearer, T time, String identity) {
		super(basedOnCoin.getSetup(), bank, basedOnCoin.getBlindedIdentity(), basedOnCoin.getCommitment(), basedOnCoin
				.getCoinSignature(), basedOnCoin.getAssetType(), basedOnCoin.getFaceValue(), identity);
		this.bearer = bearer;
		this.time = time;

		// Generated by shop
		FiniteField.Element<F> d = getSetup().getPaymentHash().hash(getBlindedIdentity(), getCommitment(),
				bearer.getIdentity(), time);

		// Generated by account holder
		r1 = d.multiply(((AbstractAccountHolderPrivate<G, F, I, T, H, H0>) basedOnCoin.getBearer()).getPrivateKey())
				.multiply(basedOnCoin.getBlindingFactor()).add(basedOnCoin.getPayerWitness1());
		r2 = d.multiply(basedOnCoin.getBlindingFactor()).add(basedOnCoin.getPayerWitness2());

		// Tested by shop
		Group.Element<G> left = getSetup().getGenerator(1).exponentiate(r1).multiply(
				getSetup().getGenerator(2).exponentiate(r2));
		Group.Element<G> right = getBlindedIdentity().exponentiate(d).multiply(getCommitment());
		LOG.fine("left == right? " + left.equals(right));
	}

	@Override
	public Shop<G, F, I, T, H, H0> getBearer() {
		return bearer;
	}

	@Override
	public FiniteField.Element<F> getR1() {
		return r1;
	}

	@Override
	public FiniteField.Element<F> getR2() {
		return r2;
	}

	@Override
	public T getTime() {
		return time;
	}

	@Override
	public boolean isSellable() {
		return false;
	}

	public AbstractSpentCoin<G, F, I, T, H, H0> transition(CoinTransferMessage<F> message) {
		if (r1.equals(message.getR1()) && r2.equals(message.getR2())) {
			return this;
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractSpentCoin<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractSpentCoin<?, ?, ?, ?, ?, ?> other = (AbstractSpentCoin<?, ?, ?, ?, ?, ?>) obj;
		return getSetup().equals(other.getSetup()) && getIssuer().equals(other.getIssuer())
				&& bearer.equals(other.bearer) && getBlindedIdentity().equals(other.getBlindedIdentity())
				&& getCommitment().equals(other.getCommitment()) && getCoinSignature().equals(other.getCoinSignature());
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, getSetup());
		hashCode = HashCodeUtil.hash(hashCode, getIssuer());
		hashCode = HashCodeUtil.hash(hashCode, bearer);
		hashCode = HashCodeUtil.hash(hashCode, getBlindedIdentity());
		hashCode = HashCodeUtil.hash(hashCode, getCommitment());
		hashCode = HashCodeUtil.hash(hashCode, getCoinSignature());
		return hashCode;
	}

}
