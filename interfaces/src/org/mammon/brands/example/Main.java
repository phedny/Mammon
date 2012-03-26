package org.mammon.brands.example;

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
	}

}
