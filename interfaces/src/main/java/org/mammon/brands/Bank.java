package org.mammon.brands;

import org.mammon.Issuer;

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
	 * @return the private key $x$ of the Bank.
	 */
	Group.Element<G> getPrivateKey();

	/**
	 * @return the public key $h$ of the Bank, which equals $h = g^x$.
	 */
	Group.Element<G> getPublicKey();

}
