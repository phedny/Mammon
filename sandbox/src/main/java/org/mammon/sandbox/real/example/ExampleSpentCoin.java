package org.mammon.sandbox.real.example;

import java.util.UUID;

import org.mammon.math.Gq;
import org.mammon.math.Z;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;

public class ExampleSpentCoin extends
		AbstractSpentCoin<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleSpentCoin(ExampleUnspentCoin basedOnCoin, AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank, ExampleShop bearer, Long time) {
		super(basedOnCoin, bank, bearer, time, UUID.randomUUID().toString());
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [SC=(" + getSpendingCommitments()[0] + ","
				+ getSpendingCommitments()[1] + "), COIN=(" + getBlindedIdentity() + "," + getCommitment() + ",("
				+ getCoinSignature().getValZ() + "," + getCoinSignature().getValA() + "," + getCoinSignature().getValB() + ","
				+ getCoinSignature().getValR() + "))]";
	}

}
