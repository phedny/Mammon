package org.mammon.sandbox.objects.example;

import java.util.UUID;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.SpentCoin;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;
import org.mammon.scheme.brands.generic.shop.AbstractShop;

public class ExampleSpentCoin
		extends
		AbstractSpentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleSpentCoin(
			ExampleUnspentCoin basedOnCoin,
			AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			ExampleShop bearer, Long time) {
		super(basedOnCoin, bank, bearer, time, UUID.randomUUID().toString());
	}

	@FromPersistent(SpentCoin.class)
	public ExampleSpentCoin(@PersistAs("setup") ExampleSetup setup, @PersistAs("issuer") ExampleBank bank,
			@PersistAs("blindedIdentity") Group.Element<ExampleGroup> blindedIdentity,
			@PersistAs("commitment") Group.Element<ExampleGroup> commitment,
			@PersistAs("coinSignature") ExampleCoinSignature coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("identity") String identity, @PersistAs("bearer") ExampleShop bearer,
			@PersistAs("time") Long time, @PersistAs("r1") FiniteField.Element<ExampleFiniteField> r1,
			@PersistAs("r2") FiniteField.Element<ExampleFiniteField> r2) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, assetType, faceValue, identity, bearer, time,
				r1, r2);
	}

	public ExampleSpentCoin(
			UnspentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> basedOnCoin,
			Bank<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			AbstractShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			Long time, String identity) {
		super(basedOnCoin, bank, bearer, time, identity);
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
