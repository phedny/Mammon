package org.mammon.sandbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mammon.math.Group;
import org.mammon.sandbox.TestUtil.AccountHolderFactory;
import org.mammon.sandbox.TestUtil.BankFactory;
import org.mammon.sandbox.TestUtil.ObjectsExampleAccountHolderFactory;
import org.mammon.sandbox.TestUtil.ObjectsExampleBankFactory;
import org.mammon.sandbox.TestUtil.RealExampleAccountHolderFactory;
import org.mammon.sandbox.TestUtil.RealExampleBankFactory;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.BankPrivate;

@RunWith(Parameterized.class)
public class AccountHolderTest {

	private final BrandsSchemeSetup setup;
	private final BankFactory bankFactory;
	private final AccountHolderFactory accountHolderFactory;
	private BankPrivate bank;
	
	public AccountHolderTest(BrandsSchemeSetup setup, BankFactory bankFactory, AccountHolderFactory accountHolderFactory) {
		this.setup = setup;
		this.bankFactory = bankFactory;
		this.accountHolderFactory = accountHolderFactory;
	}
	
	@Before
	public void createABank() {
		bank = bankFactory.getInstance(setup);
	}

	@Test
	public void aBankShouldExistPriorToCreationOfAnAccountHolder() {
		assertNotNull(bank);
	}
	
	@Test
	public void shouldBeCreatedWithBank() {
		AccountHolderPrivate accountHolder = accountHolderFactory.getInstance(setup, bank);
		assertNotNull(accountHolder);
	}

	@Test
	public void shouldHaveACertainPublicKey() {
		AccountHolderPrivate accountHolder = accountHolderFactory.getInstance(setup, bank);
		
		Group.Element expected = accountHolder.getSetup().getGenerator(1).exponentiate(accountHolder.getPrivateKey());
		
		assertEquals(expected, accountHolder.getPublicKey());
	}

	@Test
	public void shouldHaveACertainBlindedIdentity() {
		AccountHolderPrivate accountHolder = accountHolderFactory.getInstance(setup, bank);
		
		Group.Element expected = accountHolder.getPublicKey().multiply(setup.getGenerator(2)).exponentiate(bank.getPrivateKey());
		
		assertEquals(expected, accountHolder.getBlindedIdentity());
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{new org.mammon.sandbox.objects.example.ExampleSetup(), new ObjectsExampleBankFactory(), new ObjectsExampleAccountHolderFactory()});
		data.add(new Object[]{new org.mammon.sandbox.real.example.ExampleSetup(), new RealExampleBankFactory(), new RealExampleAccountHolderFactory()});
		return data;
	}
	
}
