package org.mammon.sandbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mammon.math.Group;
import org.mammon.sandbox.TestUtil.BankFactory;
import org.mammon.sandbox.TestUtil.ObjectsExampleBankFactory;
import org.mammon.sandbox.TestUtil.RealExampleBankFactory;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.bank.BankPrivate;

@RunWith(Parameterized.class)
public class BankTest {
	private final BrandsSchemeSetup setup;
	private final BankFactory factory;
	
	public BankTest(BrandsSchemeSetup setup, BankFactory factory){
		this.setup = setup;
		this.factory = factory;
	}
	
	@Test
	public void aSetupShouldExistPriorToCreationOfABank() {
		assertNotNull(setup);
	}

	@Test
	public void shouldBeCreateWithASetup() {
		Bank bank = factory.getInstance(setup);
		
		assertNotNull(bank);
	}

	@Test
	public void shouldHaveTheSetupWithWhichItIsCreated() {
		Bank bank = factory.getInstance(setup);
		
		assertEquals(setup, bank.getSetup());
	}
	
	@Test
	public void shouldHaveACertainPublicKey() {
		BankPrivate bank = factory.getInstance(setup);
		
		Group.Element expected = bank.getSetup().getGenerator(0).exponentiate(bank.getPrivateKey());
		
		assertEquals(expected, bank.getPublicKey());
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{new org.mammon.sandbox.objects.example.ExampleSetup(), new ObjectsExampleBankFactory()});
		data.add(new Object[]{new org.mammon.sandbox.real.example.ExampleSetup(), new RealExampleBankFactory()});
		return data;
		
	}
}
