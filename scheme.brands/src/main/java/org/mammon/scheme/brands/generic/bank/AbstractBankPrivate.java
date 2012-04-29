package org.mammon.scheme.brands.generic.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.BankPrivate;
import org.mammon.scheme.brands.messages.BankWitnessesRequest;
import org.mammon.scheme.brands.messages.BlindedIdentityRequest;

public abstract class AbstractBankPrivate<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractBank<G, F, T, H, H0> implements BankPrivate<G, F, T, H, H0>, Identifiable, Transactable {

	private final FiniteField.Element<F> privateKey;

	protected AbstractBankPrivate(BrandsSchemeSetup<G, F, T, H, H0> setup, FiniteField.Element<F> privateKey) {
		super(setup, setup.getGenerator(0).exponentiate(privateKey));
		this.privateKey = privateKey;
	}

	@Override
	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	public AbstractBlindedIdentity<G, F, T, H, H0> transact(BlindedIdentityRequest<G> request) {
		return newBlindedIdentity(getSetup(), this, request.getIdentity());
	}

	public String transact(BankWitnessesRequest<G> request) {
		return getIdentityForPayerIdentity(request.getIdentity().multiply(getSetup().getGenerator(2)).exponentiate(
				privateKey));
	}

	protected abstract AbstractBlindedIdentity<G, F, T, H, H0> newBlindedIdentity(
			BrandsSchemeSetup<G, F, T, H, H0> setup, AbstractBankPrivate<G, F, T, H, H0> bank,
			Group.Element<G> payerIdentity);

	protected abstract String getIdentityForPayerIdentity(Group.Element<G> payerIdentity);

	@Override
	public String getIdentity() {
		return "bank-" + getPublicKey();
	}

}