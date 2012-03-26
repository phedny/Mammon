package org.mammon.brands;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolder must return
 * <code>true</code>.
 */
public interface AccountHolder extends Bearer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup getSetup();

	/**
	 * @return the private key $u_1$ of the Account Holder, which is an element
	 *         of the group getSetup().getGroup().
	 */
	Void getPrivateKey();

	/**
	 * @return the public key or identity $I$ of the Account Holder, which is an
	 *         element of the group getSetup().getGroup() and equals $I =
	 *         g_1^{u_1}$.
	 */
	Void getPublicKey();

	/**
	 * @return the identity $z$ of the Account Holder blinded by the Bank, which
	 *         is an element of the group getSetup().getGroup() and equals $z =
	 *         (I g_2)^x$.
	 */
	Void getBlindedIdentity();

}
