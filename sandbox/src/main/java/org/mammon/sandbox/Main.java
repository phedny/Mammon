package org.mammon.sandbox;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.mammon.IOweYou;
import org.mammon.messaging.Identifiable;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.sandbox.objects.accountholder.OpeningAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleShop;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleSpentCoin;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.accountholder.AccountHolder;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		final MessagingSystem<String> messaging = new MessagingSystem<String>();

		// Setup the environment.
		final ExampleSetup setup = new ExampleSetup();

		// Setup the bank.
		final ExampleBank bank = new ExampleBank(setup, setup.getFiniteField().getRandomElement());
		messaging.addObject(bank);

		// Test handler that automatically requests issuing 1 IOweYou of value
		// EUR 1.
		messaging.registerStateHandler(AccountHolder.class,
				new StateHandler<AccountHolder, String>() {

					@Override
					public void enteredState(AccountHolder object,
							String enteredBy) {
						System.out.println("New account holder: "
								+ object.toString() + " (by " + enteredBy + ")");

						// Open account
						messaging.sendMessage(new ObtainCoinsMessage<String>(
								((Identifiable<String>) object).getIdentity(),
								1));

					}

					@Override
					public void leftState(AccountHolder object) {
					}
				});

		// Test handlers that prints any new IOweYou that we receive.
		messaging.registerStateHandler(IOweYou.class,
				new StateHandler<IOweYou, String>() {

					@Override
					public void enteredState(IOweYou object, String enteredBy) {
						System.out.println("New IOU: " + object.toString()
								+ " (by " + enteredBy + ")");
						ExampleUnspentCoin coin = (ExampleUnspentCoin) object;
						System.out.println("         " + coin);

						ExampleShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> shop = new ExampleShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
								setup, "SHOP");
						ExampleSpentCoin coin2 = new ExampleSpentCoin(coin,
								bank, shop, Long.valueOf(new Date().getTime()));
						System.out.println("         " + coin2);

						// Trigger shutdown of messaging system.
						messaging.shutdown();
					}

					@Override
					public void leftState(IOweYou object) {
					}
				});

		// Request a new account to be opened.
		OpeningAccountHolder openingAccountHolder = new OpeningAccountHolder(
				setup, bank);
		messaging.addObject(openingAccountHolder);

		// Wait for test to be finished.
		messaging.awaitTermination(500, TimeUnit.SECONDS);
		System.out.println("Done!");

	}
}
