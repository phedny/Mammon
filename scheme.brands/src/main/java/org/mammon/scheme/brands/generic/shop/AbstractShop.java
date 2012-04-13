package org.mammon.scheme.brands.generic.shop;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractReceivingCoin;
import org.mammon.scheme.brands.messages.CoinHashRequest;
import org.mammon.scheme.brands.shop.Shop;
import org.mammon.util.HashCodeUtil;
import org.mammon.util.messaging.AbstractTransactable;

public abstract class AbstractShop<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransactable implements Shop<G, F, I, T, H, H0> {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;
	private final String identity;

	public AbstractShop(BrandsSchemeSetup<G, F, I, T, H, H0> setup, String identity) {
		this.setup = setup;
		this.identity = identity;
	}

	@Override
	public String getIdentity() {
		return identity;
	}

	@Override
	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	public AbstractReceivingCoin<G, F, I, T, H, H0> transact(CoinHashRequest<G, F, I, T, H, H0> request) {
		return newAbstractReceivingCoin(getSetup(), request.getBank(), request.getBlindedIdentity(), request
				.getCommitment(), request.getCoinSignature(), request.getAssetType(), request.getFaceValue(), this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractShop<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractShop<?, ?, ?, ?, ?, ?> other = (AbstractShop<?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && identity.equals(other.identity);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, identity);
		return hashCode;
	}

	@Override
	public String toString() {
		return "ExampleShop(" + setup.hashCode() + "," + identity.toString() + ")";
	}

	protected abstract AbstractReceivingCoin<G, F, I, T, H, H0> newAbstractReceivingCoin(
			BrandsSchemeSetup<G, F, I, T, H, H0> setup, Bank<G, F, I, T, H, H0> bank, Group.Element<G> blindedIdentity,
			Group.Element<G> commitment, CoinSignature<G, F> coinSignature, AssetType assetType, Number faceValue,
			AbstractShop<G, F, I, T, H, H0> shop);

}