package org.mammon.sandbox;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.BankPrivate;

public class TestUtil {

	interface BankFactory {
		public BankPrivate getInstance(BrandsSchemeSetup setup);
	}

	static class ObjectsExampleBankFactory implements BankFactory {
	
		@Override
		public BankPrivate getInstance(BrandsSchemeSetup setup) {
			
			return new org.mammon.sandbox.objects.example.ExampleBank(setup, setup.getFiniteField().getRandomElement());
		}	
	}

	static class RealExampleBankFactory implements BankFactory {
	
		@Override
		public BankPrivate getInstance(BrandsSchemeSetup setup) {
			return new org.mammon.sandbox.real.example.ExampleBank(setup);
		}
		
	}
	
	static abstract class AccountHolderFactory {
		protected abstract AccountHolderPrivate getInstance(BrandsSchemeSetup setup, BankPrivate bank, FiniteField.Element privateKey, Group.Element publicKey, Group.Element blindedIdentity);
		final public AccountHolderPrivate getInstance(BrandsSchemeSetup setup, BankPrivate bank) {
			FiniteField.Element privateKey = setup.getFiniteField().getRandomElement();
			Group.Element publicKey = setup.getGenerator(1).exponentiate(privateKey);
			Group.Element blindedIdentity = bank.getBlindedIdentityFor(publicKey);
			return getInstance(setup, bank, privateKey, publicKey, blindedIdentity);
		}
	}

	static class ObjectsExampleAccountHolderFactory extends AccountHolderFactory {

		@Override
		protected AccountHolderPrivate getInstance(BrandsSchemeSetup setup, BankPrivate bank, FiniteField.Element privateKey, Group.Element publicKey, Group.Element blindedIdentity) {
			return new org.mammon.sandbox.objects.example.ExampleAccountHolder(setup, privateKey, publicKey, blindedIdentity, bank);
		}
		
	}

	static class RealExampleAccountHolderFactory extends AccountHolderFactory {

		@Override
		protected AccountHolderPrivate getInstance(BrandsSchemeSetup setup, BankPrivate bank, FiniteField.Element privateKey, Group.Element publicKey, Group.Element blindedIdentity) {
			return new org.mammon.sandbox.real.example.ExampleAccountHolder(setup, privateKey, publicKey, blindedIdentity, bank);
		}
		
	}

}
