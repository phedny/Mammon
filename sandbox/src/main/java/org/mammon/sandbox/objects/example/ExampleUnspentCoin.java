package org.mammon.sandbox.objects.example;

import java.util.UUID;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.generic.coin.AbstractUnspentCoin;
import org.mammon.scheme.brands.coin.CoinSignature;

public class ExampleUnspentCoin
		extends
		AbstractUnspentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleUnspentCoin(ExampleSetup setup, ExampleAccountHolder bearer, ExampleBank bank, String dualIdentity,
			FiniteField.Element<ExampleFiniteField> blindingFactor, FiniteField.Element<ExampleFiniteField> x1,
			FiniteField.Element<ExampleFiniteField> x2, Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment, FiniteField.Element<ExampleFiniteField> r,
			CoinSignature<ExampleGroup, ExampleFiniteField> coinSignature) {
		super(setup, bearer, bank, UUID.randomUUID().toString(), dualIdentity, blindingFactor, x1, x2, blindedIdentity,
				commitment, r, coinSignature);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [BF=" + getBlindingFactor() + ", PW=("
				+ getPayerWitness()[0] + "," + getPayerWitness()[1] + "), COIN=(" + getBlindedIdentity() + ","
				+ getCommitment() + ",(" + getCoinSignature().getValZ() + "," + getCoinSignature().getValA() + ","
				+ getCoinSignature().getValB() + "," + getCoinSignature().getValR() + "))]";
	}

}
