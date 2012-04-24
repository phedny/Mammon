package org.mammon.sandbox;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.mammon.math.Gq;
import org.mammon.math.Z;
import org.mammon.messaging.Identifiable;
import org.mammon.sandbox.real.accountholder.OpeningAccountHolder;
import org.mammon.sandbox.real.accountholder.ReceivingCoin;
import org.mammon.sandbox.real.accountholder.TransferringCoinOne;
import org.mammon.sandbox.real.accountholder.WithdrawingCoinOne;
import org.mammon.sandbox.real.accountholder.WithdrawingCoinTwo;
import org.mammon.sandbox.real.bank.BlindedIdentity;
import org.mammon.sandbox.real.bank.IssuedWitnesses;
import org.mammon.sandbox.real.bank.RemoteBank;
import org.mammon.sandbox.real.example.ExampleAccountHolder;
import org.mammon.sandbox.real.example.ExampleBank;
import org.mammon.sandbox.real.example.ExampleCoinSignature;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleShop;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.real.example.ExampleSpentCoin;
import org.mammon.sandbox.real.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.accountholder.AccountHolder;
import org.mammon.scheme.brands.coin.SpentCoin;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.generic.accountholder.AbstractOpeningAccountHolder;
import org.mammon.scheme.brands.generic.assettypes.EuroAssetType;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractUnspentCoin;
import org.mammon.scheme.brands.messages.BankWitnessesRequest;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.messages.BlindedIdentityRequest;
import org.mammon.scheme.brands.messages.BlindedIdentityResponse;
import org.mammon.scheme.brands.messages.CoinHashRequest;
import org.mammon.scheme.brands.messages.CoinHashResponse;
import org.mammon.scheme.brands.messages.CoinTransferMessage;
import org.mammon.scheme.brands.messages.IssueCoinsRequest;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;
import org.mammon.scheme.brands.messages.TransferToShopMessage;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		if ("1".equals(args[0])) {
			mainReal(args);
		} else if ("2".equals(args[0])) {
			mainReal2(args);
		}
	}

	// /**
	// * @param args
	// * @throws InterruptedException
	// */
	// public static void mainExample(String[] args) throws InterruptedException
	// {
	// final JsonUtil jsonUtil = new JsonUtil(EuroAssetType.class,
	// ExampleFiniteField.class, ExampleFiniteField.StaticElement.class,
	// ExampleFiniteField.AdditionElement.class,
	// ExampleFiniteField.MultiplicationElement.class,
	// ExampleFiniteField.ExponentiationElement.class, ExampleGroup.class,
	// ExampleGroup.StaticElement.class,
	// ExampleGroup.MultiplicationElement.class,
	// ExampleGroup.ExponentiationElement.class, ExampleSetup.class,
	// ExampleBank.class, BlindedIdentity.class, IssuedWitnesses.class,
	// ExampleAccountHolder.class,
	// OpeningAccountHolder.class, ExampleCoinSignature.class,
	// WithdrawingCoinOne.class,
	// WithdrawingCoinTwo.class, ExampleUnspentCoin.class,
	// BankWitnessesRequest.class,
	// BankWitnessesResponse.class, BlindedIdentityRequest.class,
	// BlindedIdentityResponse.class,
	// IssueCoinsRequest.class, IssueCoinsResponse.class,
	// ObtainCoinsMessage.class);
	// final MessagingSystem messaging = new MessagingSystem(jsonUtil);
	//
	// // Setup the environment.
	// final ExampleSetup setup = new ExampleSetup();
	//
	// // Setup the bank.
	// final AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long,
	// ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank = new
	// ExampleBank(setup, setup.getFiniteField().getRandomElement());
	// messaging.addObject(bank);
	//
	// // Test handler that automatically requests issuing 1 IOweYou of value
	// // EUR 1.
	// messaging.registerStateHandler(AccountHolder.class, new
	// StateHandler<AccountHolder>() {
	//
	// @Override
	// public void enteredState(AccountHolder object, String enteredBy) {
	// System.out.println("New account holder: " + object.toString() + " (by " +
	// enteredBy + ")");
	//
	// // Open account
	// messaging.sendMessage(new ObtainCoinsMessage(((Identifiable)
	// object).getIdentity(), 1));
	//
	// }
	//
	// @Override
	// public void leftState(AccountHolder object) {
	// }
	// });
	//
	// // Test handlers that prints any new IOweYou that we receive.
	// messaging.registerStateHandler(IOweYou.class, new StateHandler<IOweYou>()
	// {
	//
	// @Override
	// public void enteredState(IOweYou object, String enteredBy) {
	// System.out.println("New IOU: " + object.toString() + " (by " + enteredBy
	// + ")");
	// ExampleUnspentCoin coin = (ExampleUnspentCoin) object;
	// System.out.println("         " + coin);
	// System.out.println("Validity:" + coin.verifyCoinSignature());
	//
	// ExampleShop shop = new ExampleShop(setup, "SHOP");
	// ExampleSpentCoin coin2 = new ExampleSpentCoin(coin, bank, shop,
	// Long.valueOf(new Date().getTime()));
	// System.out.println("         " + coin2);
	// System.out.println("Validity:" + coin2.verifyCoinSignature());
	//
	// // Trigger shutdown of messaging system.
	// messaging.shutdown();
	// }
	//
	// @Override
	// public void leftState(IOweYou object) {
	// }
	// });
	//
	// // Request a new account to be opened.
	// AbstractOpeningAccountHolder<ExampleGroup, ExampleFiniteField, String,
	// Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>
	// openingAccountHolder = new OpeningAccountHolder(setup, bank);
	// messaging.addObject(openingAccountHolder);
	//
	// // Wait for test to be finished.
	// messaging.awaitTermination(500, TimeUnit.SECONDS);
	// System.out.println("Done!");
	// System.exit(0);
	//
	// }

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void mainReal(String[] args) throws InterruptedException, IOException {
		final String localId = "real1"; //UUID.randomUUID().toString();
		final JsonUtil jsonUtil = new JsonUtil(EuroAssetType.class, Z.class, Z.ZElement.class, Gq.class,
				Gq.GqElement.class, ExampleSetup.class, ExampleBank.class, RemoteBank.class, BlindedIdentity.class,
				IssuedWitnesses.class, ExampleAccountHolder.class, OpeningAccountHolder.class, ExampleShop.class,
				ExampleCoinSignature.class, WithdrawingCoinOne.class, WithdrawingCoinTwo.class,
				ExampleUnspentCoin.class, TransferringCoinOne.class, ReceivingCoin.class, ExampleSpentCoin.class,
				BankWitnessesRequest.class, BankWitnessesResponse.class, BlindedIdentityRequest.class,
				BlindedIdentityResponse.class, IssueCoinsRequest.class, IssueCoinsResponse.class,
				ObtainCoinsMessage.class, TransferToShopMessage.class, CoinHashRequest.class, CoinHashResponse.class,
				CoinTransferMessage.class);
		final MessagingSystem messaging = new MessagingSystem(jsonUtil, localId);

		// Setup the environment.
		final ExampleSetup setup = new ExampleSetup();
		messaging.addObject(setup.getGroup());
		messaging.addObject(setup.getFiniteField());
		messaging.addObject(setup);

		// Setup the bank.
		final AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank = new ExampleBank(
				setup, setup.getFiniteField().getRandomElement());
		messaging.addObject(bank, true);

		// Setup the shop.
		final ExampleShop shop = new ExampleShop(setup, "SHOP");
		messaging.addObject(shop);

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

		// Test handlers that prints any new UnspentCoin that we receive.
		messaging.registerStateHandler(UnspentCoin.class, new StateHandler<UnspentCoin>() {

			@Override
			public void enteredState(UnspentCoin object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				System.out.println("Validity:" + object.verifyCoinSignature());

				// ExampleShop shop = new ExampleShop(setup, "SHOP");
				// ExampleSpentCoin coin2 = new ExampleSpentCoin(coin, bank,
				// shop, Long.valueOf(new Date().getTime()));
				// System.out.println("         " + coin2);
				// System.out.println("Validity:" +
				// coin2.verifyCoinSignature());
				//
				// // Trigger shutdown of messaging system.
				// messaging.shutdown();

				// Transfer coin
				messaging.sendMessage(new TransferToShopMessage(((AbstractUnspentCoin) object).getIdentity(), "SHOP"));

			}

			@Override
			public void leftState(UnspentCoin object) {
			}
		});

		// Test handlers that prints any new SpentCoin that we receive.
		messaging.registerStateHandler(SpentCoin.class, new StateHandler<SpentCoin>() {

			@Override
			public void enteredState(SpentCoin object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				System.out.println("Validity:" + object.verifyCoinSignature());

				System.exit(0);
			}

			@Override
			public void leftState(SpentCoin object) {
			}

		});

		// Request a new account to be opened.
//		AbstractOpeningAccountHolder<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> openingAccountHolder = new OpeningAccountHolder(
//				setup, bank);
//		messaging.addObject(openingAccountHolder);

		// Wait for test to be finished.
		messaging.awaitTermination(500, TimeUnit.SECONDS);
		System.out.println("Done!");
		System.exit(0);

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void mainReal2(String[] args) throws InterruptedException, IOException {
		final String localId = "real2"; //UUID.randomUUID().toString();
		final JsonUtil jsonUtil = new JsonUtil(EuroAssetType.class, Z.class, Z.ZElement.class, Gq.class,
				Gq.GqElement.class, ExampleSetup.class, ExampleBank.class, RemoteBank.class, BlindedIdentity.class,
				IssuedWitnesses.class, ExampleAccountHolder.class, OpeningAccountHolder.class, ExampleShop.class,
				ExampleCoinSignature.class, WithdrawingCoinOne.class, WithdrawingCoinTwo.class,
				ExampleUnspentCoin.class, TransferringCoinOne.class, ReceivingCoin.class, ExampleSpentCoin.class,
				BankWitnessesRequest.class, BankWitnessesResponse.class, BlindedIdentityRequest.class,
				BlindedIdentityResponse.class, IssueCoinsRequest.class, IssueCoinsResponse.class,
				ObtainCoinsMessage.class, TransferToShopMessage.class, CoinHashRequest.class, CoinHashResponse.class,
				CoinTransferMessage.class);
		final MessagingSystem messaging = new MessagingSystem(jsonUtil, localId);

		// Setup the environment.
		final ExampleSetup setup = new ExampleSetup();
		messaging.addObject(setup.getGroup());
		messaging.addObject(setup.getFiniteField());
		messaging.addObject(setup);

		// Setup the bank.
//		final AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank = new ExampleBank(
//				setup, setup.getFiniteField().getRandomElement());
//		messaging.addObject(bank);
//		final String remoteBankId = messaging.publish(localId, bank);
//		final RemoteBank remoteBank = new RemoteBank(setup, (Gq.GqElement) bank.getPublicKey(), localId + ":" + remoteBankId);
//		messaging.addObject(remoteBank);

		// Setup the shop.
		final ExampleShop shop = new ExampleShop(setup, "SHOP");
		messaging.addObject(shop);
		
		messaging.registerStateHandler(RemoteBank.class, new StateHandler<RemoteBank>() {

			@Override
			public void enteredState(RemoteBank object, String enteredBy) {

				// Request a new account to be opened.
				AbstractOpeningAccountHolder<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> openingAccountHolder = new OpeningAccountHolder(
						setup, object);
				messaging.addObject(openingAccountHolder);

			}

			@Override
			public void leftState(RemoteBank object) {
				// TODO Auto-generated method stub
				
			}});

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

		// Test handlers that prints any new UnspentCoin that we receive.
		messaging.registerStateHandler(UnspentCoin.class, new StateHandler<UnspentCoin>() {

			@Override
			public void enteredState(UnspentCoin object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				System.out.println("Validity:" + object.verifyCoinSignature());

				// ExampleShop shop = new ExampleShop(setup, "SHOP");
				// ExampleSpentCoin coin2 = new ExampleSpentCoin(coin, bank,
				// shop, Long.valueOf(new Date().getTime()));
				// System.out.println("         " + coin2);
				// System.out.println("Validity:" +
				// coin2.verifyCoinSignature());
				//
				// // Trigger shutdown of messaging system.
				// messaging.shutdown();

				// Transfer coin
				messaging.sendMessage(new TransferToShopMessage(((AbstractUnspentCoin) object).getIdentity(), "SHOP"));

			}

			@Override
			public void leftState(UnspentCoin object) {
			}
		});

		// Test handlers that prints any new SpentCoin that we receive.
		messaging.registerStateHandler(SpentCoin.class, new StateHandler<SpentCoin>() {

			@Override
			public void enteredState(SpentCoin object, String enteredBy) {
				System.out.println("New IOU: " + object.toString() + " (by " + enteredBy + ")");
				System.out.println("Validity:" + object.verifyCoinSignature());

				System.exit(0);
			}

			@Override
			public void leftState(SpentCoin object) {
			}

		});

		messaging.connect("localhost");

		// Wait for test to be finished.
		messaging.awaitTermination(500, TimeUnit.SECONDS);
		System.out.println("Done!");
		System.exit(0);

	}
}
