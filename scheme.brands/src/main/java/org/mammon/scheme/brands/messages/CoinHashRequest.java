package org.mammon.scheme.brands.messages;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;

public class CoinHashRequest<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		implements DirectedMessage {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final Bank<G, F, I, T, H, H0> bank;

	private final Group.Element<G> blindedIdentity;

	private final Group.Element<G> commitment;

	private final CoinSignature<G, F> coinSignature;

	private final AssetType assetType;

	private final Number faceValue;

	private final String destination;

	@FromPersistent(CoinHashRequest.class)
	public CoinHashRequest(@PersistAs("setup") BrandsSchemeSetup<G, F, I, T, H, H0> setup,
			@PersistAs("bank") Bank<G, F, I, T, H, H0> bank,
			@PersistAs("blindedIdentity") Group.Element<G> blindedIdentity,
			@PersistAs("commitment") Group.Element<G> commitment,
			@PersistAs("coinSignature") CoinSignature<G, F> coinSignature, @PersistAs("assetType") AssetType assetType,
			@PersistAs("faceValue") Number faceValue, @PersistAs("destination") String destination) {
		this.setup = setup;
		this.bank = bank;
		this.blindedIdentity = blindedIdentity;
		this.commitment = commitment;
		this.coinSignature = coinSignature;
		this.assetType = assetType;
		this.faceValue = faceValue;
		this.destination = destination;
	}

	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	public Bank<G, F, I, T, H, H0> getBank() {
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

	@Override
	public String getDestination() {
		return destination;
	}

}
