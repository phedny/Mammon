package org.mammon.scheme.brands.generic.coin;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.Coin;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractCoin<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractTransitionable implements Coin<G, F, T, H, H0> {

	private final BrandsSchemeSetup<G, F, T, H, H0> setup;

	private final Bank<G, F, T, H, H0> bank;

	private final Group.Element<G> blindedIdentity;

	private final Group.Element<G> commitment;

	private final CoinSignature<G, F> coinSignature;

	private final AssetType assetType;

	private final Number faceValue;

	private final String identity;

	public AbstractCoin(BrandsSchemeSetup<G, F, T, H, H0> setup, Bank<G, F, T, H, H0> bank,
			Element<G> blindedIdentity, Element<G> commitment, CoinSignature<G, F> coinSignature, AssetType assetType,
			Number faceValue, String identity) {
		this.setup = setup;
		this.bank = bank;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.coinSignature = coinSignature;
		this.assetType = assetType;
		this.faceValue = faceValue;
		this.identity = identity;
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public CoinSignature<G, F> getCoinSignature() {
		return coinSignature;
	}

	@Override
	public Element<G> getCommitment() {
		return commitment;
	}

	@Override
	public Bank<G, F, T, H, H0> getIssuer() {
		return bank;
	}

	@Override
	public BrandsSchemeSetup<G, F, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public AssetType getAssetType() {
		return assetType;
	}

	@Override
	public Number getFaceValue() {
		return faceValue;
	}

	@Override
	public String getIdentity() {
		return identity;
	}

	@Override
	public boolean verifyCoinSignature() {
		FiniteField.Element<F> hash = getSetup().getSignatureHash().hash(getBlindedIdentity(), getCommitment(),
				getCoinSignature().getValZ(), getCoinSignature().getValA(), getCoinSignature().getValB());

		Group.Element<G> left = getSetup().getGenerator(0).exponentiate(getCoinSignature().getValR());
		Group.Element<G> right = getIssuer().getPublicKey().exponentiate(hash).multiply(getCoinSignature().getValA());
		if (!left.equals(right)) {
			return false;
		}

		left = getBlindedIdentity().exponentiate(getCoinSignature().getValR());
		right = getCoinSignature().getValZ().exponentiate(hash).multiply(getCoinSignature().getValB());
		return left.equals(right);
	}

}
