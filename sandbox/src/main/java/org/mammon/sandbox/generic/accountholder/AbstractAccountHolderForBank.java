package org.mammon.sandbox.generic.accountholder;

import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolderForBank;

public abstract class AbstractAccountHolderForBank<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I> extends AbstractAccountHolder<G, S, T, H, H0, I>
		implements AccountHolderForBank<G, S, T, H, H0>, Identifiable<I>, Transactable {

	private final Element<G> publicKey;

	public AbstractAccountHolderForBank(BrandsSchemeSetup<G, S, T, H, H0> setup, Element<G> publicKey,
			Element<G> blindedIdentity) {
		super(setup, blindedIdentity);
		this.publicKey = publicKey;
	}

	@Override
	public Element<G> getPublicKey() {
		return publicKey;
	}

}