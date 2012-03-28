package org.mammon.scheme.brands.bank;

import org.mammon.Issuer;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, an issuer of an IOU is called the bank.
 */
public interface Bank<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends Issuer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, S, T, H, H0> getSetup();

	/**
	 * @return the public key $h$ of the BankPrivate, which equals $h = g^x$.
	 */
	Group.Element<G> getPublicKey();

}