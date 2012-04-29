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
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.messages.CoinHashRequest;
import org.mammon.scheme.brands.messages.CoinHashResponse;
import org.mammon.scheme.brands.messages.CoinTransferMessage;
import org.mammon.scheme.brands.messages.TransferToShopMessage;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractTransferringCoinOne<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransitionable implements Identifiable, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final Bank<G, F, I, T, H, H0> bank;

	private final AccountHolderPrivate<G, F, I, T, H, H0> bearer;

	private final Group.Element<G> blindedIdentity;

	private final Group.Element<G> commitment;

	private final CoinSignature<G, F> coinSignature;

	private final AssetType assetType;

	private final Number faceValue;

	private final FiniteField.Element<F> s;

	private final FiniteField.Element<F> x1;

	private final FiniteField.Element<F> x2;

	private final String identity;

	private final String shop;

	public AbstractTransferringCoinOne(BrandsSchemeSetup<G, F, I, T, H, H0> setup, Bank<G, F, I, T, H, H0> bank,
			AccountHolderPrivate<G, F, I, T, H, H0> bearer, Group.Element<G> blindedIdentity,
			Group.Element<G> commitment, CoinSignature<G, F> coinSignature, AssetType assetType, Number faceValue,
			FiniteField.Element<F> s, FiniteField.Element<F> x1, FiniteField.Element<F> x2, String identity, String shop) {
		this.setup = setup;
		this.bank = bank;
		this.bearer = bearer;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.coinSignature = coinSignature;
		this.assetType = assetType;
		this.faceValue = faceValue;
		this.s = s;
		this.x1 = x1;
		this.x2 = x2;
		this.identity = identity;
		this.shop = shop;
	}

	@Override
	public String getIdentity() {
		return identity;
	}

	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	public Bank<G, F, I, T, H, H0> getBank() {
		return bank;
	}

	public AccountHolderPrivate<G, F, I, T, H, H0> getBearer() {
		return bearer;
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

	public FiniteField.Element<F> getS() {
		return s;
	}

	public FiniteField.Element<F> getX1() {
		return x1;
	}

	public FiniteField.Element<F> getX2() {
		return x2;
	}

	public String getShop() {
		return shop;
	}

	@Override
	public Message emitMessage() {
		return new CoinHashRequest<G, F, I, T, H, H0>(setup, bank, blindedIdentity, commitment, coinSignature,
				assetType, faceValue, shop);
	}

	public CoinTransferMessage<F> transition(CoinHashResponse<F> response) {
		FiniteField.Element<F> r1 = response.getHash().multiply(getBearer().getPrivateKey()).multiply(getS()).add(
				getX1());
		FiniteField.Element<F> r2 = response.getHash().multiply(getS()).add(getX2());
		return new CoinTransferMessage<F>(r1, r2);
	}

	public Object transition(TransferToShopMessage message) {
		if (getIdentity().equals(message.getDestination()) && getShop().equals(message.getShop())) {
			return this;
		}
		return null;
	}

}
