package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.generic.coin.AbstractUnspentCoin;

public class ExampleUnspentCoin
		extends
		AbstractUnspentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public ExampleUnspentCoin(ExampleSetup setup, ExampleAccountHolder bearer,
			ExampleBank bank,
			FiniteField.Element<ExampleFiniteField> blindingFactor,
			FiniteField.Element<ExampleFiniteField> x1,
			FiniteField.Element<ExampleFiniteField> x2,
			Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment, Object[] coinSignature) {
		super(setup, bearer, bank, blindingFactor, x1, x2, blindedIdentity,
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
