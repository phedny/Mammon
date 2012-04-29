package org.mammon.sandbox.real.example;

import java.util.UUID;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.coin.SpentCoin;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;

public class ExampleSpentCoin extends
		AbstractSpentCoin<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleSpentCoin(ExampleUnspentCoin basedOnCoin,
			AbstractBankPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			ExampleShop bearer, Long time) {
		super(basedOnCoin, bank, bearer, time, UUID.randomUUID().toString());
	}

	@FromPersistent(SpentCoin.class)
	public ExampleSpentCoin(@PersistAs("setup") ExampleSetup setup, @PersistAs("issuer") AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("commitment") Group.Element<Gq> commitment,
			@PersistAs("coinSignature") ExampleCoinSignature coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("identity") String identity, @PersistAs("bearer") ExampleShop bearer,
			@PersistAs("time") Long time, @PersistAs("r1") FiniteField.Element<Z> r1,
			@PersistAs("r2") FiniteField.Element<Z> r2) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, assetType, faceValue, identity, bearer, time,
				r1, r2);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [SC=(" + getR1() + "," + getR2()
				+ "), COIN=(" + getBlindedIdentity() + "," + getCommitment() + ",(" + getCoinSignature().getValZ()
				+ "," + getCoinSignature().getValA() + "," + getCoinSignature().getValB() + ","
				+ getCoinSignature().getValR() + "))]";
	}

	@Override
	public Long getTime() {
		return super.getTime();
	}

}
