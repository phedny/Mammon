package org.mammon.sandbox;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.mammon.IOweYou;
import org.mammon.messaging.Identifiable;
import org.mammon.sandbox.objects.accountholder.OpeningAccountHolder;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinTwo;
import org.mammon.sandbox.objects.bank.BlindedIdentity;
import org.mammon.sandbox.objects.bank.IssuedWitnesses;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleCoinSignature;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleShop;
import org.mammon.sandbox.objects.example.ExampleSpentCoin;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.accountholder.AccountHolder;
import org.mammon.scheme.brands.generic.assettypes.EuroAssetType;
import org.mammon.scheme.brands.messages.BankWitnessesRequest;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.messages.BlindedIdentityRequest;
import org.mammon.scheme.brands.messages.BlindedIdentityResponse;
import org.mammon.scheme.brands.messages.IssueCoinsRequest;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		final JsonUtil jsonUtil = new JsonUtil(EuroAssetType.class,
				ExampleFiniteField.class, ExampleFiniteField.StaticElement.class,
				ExampleFiniteField.AdditionElement.class, ExampleFiniteField.MultiplicationElement.class,
				ExampleFiniteField.ExponentiationElement.class, ExampleGroup.class, ExampleGroup.StaticElement.class,
				ExampleGroup.MultiplicationElement.class, ExampleGroup.ExponentiationElement.class, ExampleSetup.class,
				ExampleBank.class, BlindedIdentity.class, IssuedWitnesses.class, ExampleAccountHolder.class,
				OpeningAccountHolder.class, ExampleCoinSignature.class, WithdrawingCoinOne.class,
				WithdrawingCoinTwo.class, ExampleUnspentCoin.class, BankWitnessesRequest.class,
				BankWitnessesResponse.class, BlindedIdentityRequest.class, BlindedIdentityResponse.class,
				IssueCoinsRequest.class, IssueCoinsResponse.class, ObtainCoinsMessage.class);
		final MessagingSystem messaging = new MessagingSystem(jsonUtil);

		// Setup the environment.
		final ExampleSetup setup = new ExampleSetup();

		// Setup the bank.
		final ExampleBank bank = new ExampleBank(setup, setup.getFiniteField().getRandomElement());
		messaging.addObject(bank);

		// Test handler that automatically requests issuing 1 IOweYou of value
		// EUR 1.
		messaging.registerStateHandler(AccountHolder.class, new StateHandler<AccountHolder>() {

			@Override
			public void enteredState(AccountHolder object, String enteredBy) {
				System.out.println("New account holder: " + object.toString() + " (by " + enteredBy + ")");

				// Open account
				messaging.sendMessage(new ObtainCoinsMessage(((Identifiable) object).getIdentity(), 1));

			}

			@Override
			public void leftState(AccountHolder object) {
			}
		});

		// Test handlers that prints any new IOweYou that we receive.
		messaging.registerStateHandler(IOweYou.class, new StateHandler<IOweYou>() {

			@Override
			public void enteredState(IOweYou object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				ExampleUnspentCoin coin = (ExampleUnspentCoin) object;
				System.out.println("         " + coin);
				System.out.println("Validity:" + coin.verifyCoinSignature());

				ExampleShop shop = new ExampleShop(setup, "SHOP");
				ExampleSpentCoin coin2 = new ExampleSpentCoin(coin, bank, shop, Long.valueOf(new Date().getTime()));
				System.out.println("         " + coin2);
				System.out.println("Validity:" + coin2.verifyCoinSignature());

				// Trigger shutdown of messaging system.
				messaging.shutdown();
			}

			@Override
			public void leftState(IOweYou object) {
			}
		});

		// Request a new account to be opened.
		OpeningAccountHolder openingAccountHolder = new OpeningAccountHolder(setup, bank);
		messaging.addObject(openingAccountHolder);

		// Wait for test to be finished.
		messaging.awaitTermination(500, TimeUnit.SECONDS);
		System.out.println("Done!");
		System.exit(0);

	}
}
