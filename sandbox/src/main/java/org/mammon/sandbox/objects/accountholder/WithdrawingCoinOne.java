package org.mammon.sandbox.objects.accountholder;

import java.util.UUID;

import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinTwo;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;

public class WithdrawingCoinOne
		extends
		AbstractWithdrawingCoinOne<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	private final String identity = UUID.randomUUID().toString();

	public WithdrawingCoinOne(ExampleAccountHolder accountHolder, ExampleBank bank, Element<ExampleGroup> publicKey,
			int count) {
		super(accountHolder, bank, publicKey, count);
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected AbstractWithdrawingCoinTwo<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> newWithdrawingCoinTwo(
			Element<ExampleGroup>[] witnesses, Element<ExampleGroup>[] challenges,
			Element<ExampleGroup>[] blindingFactor, Element<ExampleGroup>[] payerWitness,
			Element<ExampleGroup>[] secondWitness, Element<ExampleGroup>[] blindedIdentity,
			Element<ExampleGroup>[] commitment) {
		return new WithdrawingCoinTwo((ExampleSetup) getSetup(), (ExampleAccountHolder) getAccountHolder(),
				getIdentity(), (ExampleBank) getBank(), getPublicKey(), getCount(), blindingFactor, payerWitness,
				secondWitness, blindedIdentity, commitment, witnesses, challenges);
	}

}
