package org.mammon.sandbox;

import java.util.Date;

import org.mammon.IOweYou;
import org.mammon.brands.AccountHolder;
import org.mammon.messaging.Identifiable;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.sandbox.objects.accountholder.OpeningAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleShop;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleSpentCoin;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final MessagingSystem<String> messaging = new MessagingSystem<String>();

		// Setup the environment.
		final ExampleSetup setup = new ExampleSetup();

		// Setup the bank.
		final ExampleBank<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank = new ExampleBank<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup);
		messaging.addObject(bank);

		// Test handler that automatically requests issuing 1 IOweYou of value
		// EUR 1.
		messaging.registerStateHandler(AccountHolder.class, new StateHandler<AccountHolder, String>() {

			@Override
			public void enteredState(AccountHolder object, String enteredBy) {
				System.out.println("New account holder: " + object.toString() + " (by " + enteredBy + ")");

				// Open account
				messaging.sendMessage(new ObtainCoinsMessage<String>(((Identifiable<String>) object).getIdentity(), 1));

			}

			@Override
			public void leftState(AccountHolder object) {
			}
		});

		// Test handlers that prints any new IOweYou that we receive.
		messaging.registerStateHandler(IOweYou.class, new StateHandler<IOweYou, String>() {

			@Override
			public void enteredState(IOweYou object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				ExampleUnspentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> coin = (ExampleUnspentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) object;
				System.out.println("         " + coin);

				ExampleShop<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> shop = new ExampleShop<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
						setup, "SHOP");
				ExampleSpentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> coin2 = new ExampleSpentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
						coin, bank, shop, Long.valueOf(new Date().getTime()));
				System.out.println(coin2);
			}

			@Override
			public void leftState(IOweYou object) {
			}
		});

		// Request a new account to be opened.
		OpeningAccountHolder<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> openingAccountHolder = new OpeningAccountHolder<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup, bank);
		messaging.addObject(openingAccountHolder);

	}
}
