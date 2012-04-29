package org.mammon.scheme.brands.bank;

import org.mammon.Issuer;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, an issuer of an IOU is called the bank.
 */
public interface Bank<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends Issuer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, F, T, H, H0> getSetup();

	/**
	 * @return the public key $h$ of the BankPrivate, which equals $h = g^x$.
	 */
	Group.Element<G> getPublicKey();

}