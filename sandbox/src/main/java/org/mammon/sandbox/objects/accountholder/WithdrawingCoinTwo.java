package org.mammon.sandbox.objects.accountholder;

import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinTwo;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.Group.Element;

public class WithdrawingCoinTwo
		extends
		AbstractWithdrawingCoinTwo<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	private final String identity;

	public WithdrawingCoinTwo(ExampleSetup setup, ExampleAccountHolder accountHolder, String identity,
			ExampleBank bank, Element<ExampleGroup> publicKey, int count, Element<ExampleGroup>[] blindingFactor,
			Element<ExampleGroup>[] payerWitness, Element<ExampleGroup>[] secondWitness,
			Element<ExampleGroup>[] blindedIdentity, Element<ExampleGroup>[] commitment,
			Element<ExampleGroup>[] witnesses, Element<ExampleGroup>[] challenges) {
		super(setup, accountHolder, bank, publicKey, count, blindingFactor, payerWitness, secondWitness,
				blindedIdentity, commitment, witnesses, challenges);
		this.identity = identity;
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected ExampleUnspentCoin newUnspentCoin(Element<ExampleGroup>[] coinSignature) {
		return new ExampleUnspentCoin((ExampleSetup) getSetup(), (ExampleAccountHolder) getAccountHolder(),
				(ExampleBank) getBank(), getBlindingFactor()[0], getPayerWitness(), getBlindedIdentity()[0],
				getCommitment()[0], coinSignature);
	}

}
