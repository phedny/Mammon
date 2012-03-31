package org.mammon.sandbox.objects.example;

import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;

public class ExampleSpentCoin extends
		AbstractSpentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleSpentCoin(ExampleUnspentCoin basedOnCoin, ExampleBank bank, ExampleShop bearer, Long time) {
		super(basedOnCoin, bank, bearer, time);
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
