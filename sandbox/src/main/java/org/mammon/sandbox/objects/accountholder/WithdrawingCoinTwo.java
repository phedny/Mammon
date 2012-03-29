package org.mammon.sandbox.objects.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinTwo;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;

public class WithdrawingCoinTwo
		extends
		AbstractWithdrawingCoinTwo<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	private final String identity;

	public WithdrawingCoinTwo(ExampleSetup setup,
			ExampleAccountHolder accountHolder, String identity,
			ExampleBank bank, Group.Element<ExampleGroup> publicKey, int count,
			FiniteField.Element<ExampleFiniteField>[] blindingFactor,
			FiniteField.Element<ExampleFiniteField>[] payerWitness,
			FiniteField.Element<ExampleFiniteField>[] secondWitness,
			Group.Element<ExampleGroup>[] blindedIdentity,
			Group.Element<ExampleGroup>[] commitment,
			Group.Element<ExampleGroup>[] witnesses,
			FiniteField.Element<ExampleFiniteField>[] challenges) {
		super(setup, accountHolder, bank, publicKey, count, blindingFactor,
				payerWitness, secondWitness, blindedIdentity, commitment,
				witnesses, challenges);
		this.identity = identity;
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected ExampleUnspentCoin newUnspentCoin(Object[] coinSignature) {
		return new ExampleUnspentCoin((ExampleSetup) getSetup(),
				(ExampleAccountHolder) getAccountHolder(),
				(ExampleBank) getBank(), getBlindingFactor()[0],
				getPayerWitness(), getBlindedIdentity()[0], getCommitment()[0],
				coinSignature);
	}

}
