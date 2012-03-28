package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.generic.coin.AbstractSpentCoin;

public class ExampleSpentCoin extends
		AbstractSpentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	@SuppressWarnings("unchecked")
	public ExampleSpentCoin(ExampleUnspentCoin basedOnCoin, ExampleBank bank, ExampleShop bearer, Long time) {
		super(basedOnCoin, bank, bearer, time);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [SC=(" + getSpendingCommitments()[0] + ","
				+ getSpendingCommitments()[1] + "), COIN=(" + getBlindedIdentity() + "," + getCommitment() + ",("
				+ getCoinSignature()[0] + "," + getCoinSignature()[1] + "," + getCoinSignature()[2] + ","
				+ getCoinSignature()[3] + "))]";
	}

}
