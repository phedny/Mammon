package org.mammon.sandbox;

import java.util.Date;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExampleSetup setup = new ExampleSetup();
		ExampleBank<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank = new ExampleBank<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup);
		ExampleAccountHolder<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> ah = new ExampleAccountHolder<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup, bank);
		ExampleUnspentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> coin = new ExampleUnspentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup, ah, bank);
		System.out.println(coin);

		ExampleShop<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> shop = new ExampleShop<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				setup, "SHOP");
		ExampleSpentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> coin2 = new ExampleSpentCoin<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>(
				coin, bank, shop, Long.valueOf(new Date().getTime()));
		System.out.println(coin2);
	}
}
