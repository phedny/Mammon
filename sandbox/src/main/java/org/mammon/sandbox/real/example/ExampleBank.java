package org.mammon.sandbox.real.example;

import org.mammon.Issuer;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.math.Group.Element;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.PublishAs;
import org.mammon.sandbox.real.bank.BlindedIdentity;
import org.mammon.sandbox.real.bank.ConvertToRemoteBank;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBlindedIdentity;

@PublishAs(ConvertToRemoteBank.class)
public class ExampleBank
		extends
		AbstractBankPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Issuer.class)
	public ExampleBank(
			@PersistAs("setup") BrandsSchemeSetup<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			@PersistAs("privateKey") FiniteField.Element<Z> privateKey) {
		super(setup, privateKey);
	}

	@Override
	protected AbstractBlindedIdentity<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newBlindedIdentity(
			BrandsSchemeSetup<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBankPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<Gq> payerIdentity) {
		return new BlindedIdentity((ExampleSetup) setup, (AbstractBankPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank, payerIdentity);
	}

	@Override
	protected String getIdentityForPayerIdentity(Element<Gq> payerIdentity) {
		return "blindedidentity-" + getIdentity() + "-" + payerIdentity;
	}
}
