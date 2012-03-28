package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.generic.coin.AbstractUnspentCoin;
import org.mammon.scheme.brands.Group.Element;

public class ExampleUnspentCoin
		extends
		AbstractUnspentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public ExampleUnspentCoin(ExampleSetup setup, ExampleAccountHolder bearer,
			ExampleBank bank, Element<ExampleGroup> blindingFactor, Element<ExampleGroup>[] payerWitness,
			Element<ExampleGroup> blindedIdentity, Element<ExampleGroup> commitment, Element<ExampleGroup>[] coinSignature) {
		super(setup, bearer, bank, blindingFactor, payerWitness, blindedIdentity, commitment, coinSignature);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [BF=" + getBlindingFactor() + ", PW=("
				+ getPayerWitness()[0] + "," + getPayerWitness()[1] + "), COIN=(" + getBlindedIdentity() + ","
				+ getCommitment() + ",(" + getCoinSignature()[0] + "," + getCoinSignature()[1] + ","
				+ getCoinSignature()[2] + "," + getCoinSignature()[3] + "))]";
	}

}
