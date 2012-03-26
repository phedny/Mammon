package org.mammon.brands;

import org.mammon.Issuer;

/**
 * In the Brands scheme, an issuer of an IOU is called the bank.
 */
public interface Bank extends Issuer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup getSetup();

	/**
	 * @return the private key $x$ of the Bank, which is an element of the group
	 *         getSetup().getGroup().
	 */
	Void getPrivateKey();

	/**
	 * @return the public key $h$ of the Bank, which is an element of the group
	 *         getSetup().getGroup() and equals $h = g^x$.
	 */
	Void getPublicKey();

}
