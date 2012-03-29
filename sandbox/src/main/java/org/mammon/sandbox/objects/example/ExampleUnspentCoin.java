package org.mammon.sandbox.objects.example;

import org.mammon.math.Group.Element;
import org.mammon.sandbox.generic.coin.AbstractUnspentCoin;

public class ExampleUnspentCoin
		extends
		AbstractUnspentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public ExampleUnspentCoin(ExampleSetup setup, ExampleAccountHolder bearer,
			ExampleBank bank,
			org.mammon.math.FiniteField.Element<ExampleFiniteField> element,
			org.mammon.math.FiniteField.Element<ExampleFiniteField>[] elements,
			Element<ExampleGroup> blindedIdentity,
			Element<ExampleGroup> commitment, Object[] coinSignature) {
		super(setup, bearer, bank, element, elements, blindedIdentity,
				commitment, coinSignature);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, "
				+ getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [BF="
				+ getBlindingFactor() + ", PW=(" + getPayerWitness()[0] + ","
				+ getPayerWitness()[1] + "), COIN=(" + getBlindedIdentity()
				+ "," + getCommitment() + ",(" + getCoinSignature()[0] + ","
				+ getCoinSignature()[1] + "," + getCoinSignature()[2] + ","
				+ getCoinSignature()[3] + "))]";
	}

}
