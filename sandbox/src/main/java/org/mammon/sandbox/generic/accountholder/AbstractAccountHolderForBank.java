package org.mammon.sandbox.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;

public abstract class AbstractAccountHolderForBank<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I> extends AbstractAccountHolder<G, F, S, T, H, H0, I>
		implements AccountHolderForBank<G, F, S, T, H, H0>, Identifiable<I>, Transactable {

	private final Element<G> publicKey;

	public AbstractAccountHolderForBank(BrandsSchemeSetup<G, F, S, T, H, H0> setup, Element<G> publicKey,
			Element<G> blindedIdentity) {
		super(setup, blindedIdentity);
		this.publicKey = publicKey;
	}

	@Override
	public Element<G> getPublicKey() {
		return publicKey;
	}

}