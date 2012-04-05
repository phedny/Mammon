package org.mammon.sandbox.objects.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.bank.AbstractIssuedWitnesses;

public class IssuedWitnesses
		extends
		AbstractIssuedWitnesses<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractIssuedWitnesses.class)
	public IssuedWitnesses(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") ExampleBank bank,
			@PersistAs("payerIdentity") Group.Element<ExampleGroup> payerIdentity,
			@PersistAs("w") FiniteField.Element<ExampleFiniteField> w) {
		super(setup, bank, payerIdentity, w);
	}

	@Override
	public String getIdentity() {
		return "issuedwitnesses-" + getBank().getIdentity() + "-" + getPayerIdentity() + "-" + getA();
	}

}
