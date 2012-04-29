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
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.shop.AbstractShop;
import org.mammon.scheme.brands.messages.CoinHashResponse;
import org.mammon.scheme.brands.messages.CoinTransferMessage;
import org.mammon.scheme.brands.shop.Shop;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractReceivingCoin<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractTransitionable implements Identifiable, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, T, H, H0> setup;

	private final AbstractBank<G, F, T, H, H0> bank;

	private final Group.Element<G> blindedIdentity;

	private final Group.Element<G> commitment;

	private final CoinSignature<G, F> coinSignature;

	private final AssetType assetType;

	private final Number faceValue;

	private final AbstractShop<G, F, T, H, H0> shop;

	private final T time;

	public AbstractReceivingCoin(BrandsSchemeSetup<G, F, T, H, H0> setup, AbstractBank<G, F, T, H, H0> bank,
			Group.Element<G> blindedIdentity, Group.Element<G> commitment, CoinSignature<G, F> coinSignature,
			AssetType assetType, Number faceValue, AbstractShop<G, F, T, H, H0> shop, T time) {
		this.setup = setup;
		this.bank = bank;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.coinSignature = coinSignature;
		this.assetType = assetType;
		this.faceValue = faceValue;
		this.shop = shop;
		this.time = time;
	}

	public BrandsSchemeSetup<G, F, T, H, H0> getSetup() {
		return setup;
	}

	public AbstractBank<G, F, T, H, H0> getBank() {
		return bank;
	}

	public Group.Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	public Group.Element<G> getCommitment() {
		return commitment;
	}

	public CoinSignature<G, F> getCoinSignature() {
		return coinSignature;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public Number getFaceValue() {
		return faceValue;
	}

	public AbstractShop<G, F, T, H, H0> getShop() {
		return shop;
	}

	public T getTime() {
		return time;
	}

	@Override
	public String getIdentity() {
		return "receivingcoin-" + shop + "-" + blindedIdentity + "-" + commitment;
	}

	@Override
	public Message emitMessage() {
		return new CoinHashResponse<F>(getHash());
	}

	public FiniteField.Element<F> getHash() {
		return getSetup().getPaymentHash().hash(getBlindedIdentity(), getCommitment(), getShop().getIdentity(),
				getTime());
	}

	public AbstractSpentCoin<G, F, T, H, H0> transition(CoinTransferMessage<F> message) {
		return newAbstractSpentCoin(getSetup(), getBank(), getBlindedIdentity(), getCommitment(), getCoinSignature(),
				getAssetType(), getFaceValue(), getIdentity(), getShop(), getTime(), message.getR1(), message.getR2());
	}

	protected abstract AbstractSpentCoin<G, F, T, H, H0> newAbstractSpentCoin(
			BrandsSchemeSetup<G, F, T, H, H0> setup, AbstractBank<G, F, T, H, H0> bank,
			Group.Element<G> blindedIdentity, Group.Element<G> commitment, CoinSignature<G, F> coinSignature,
			AssetType assetType, Number faceValue, String identity, Shop<G, F, T, H, H0> bearer, T time,
			FiniteField.Element<F> r1, FiniteField.Element<F> r2);

}
