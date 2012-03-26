package org.mammon.brands;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolder must return
 * <code>true</code>.
 */
public interface AccountHolder<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends Bearer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, S, T, H, H0> getSetup();

	/**
	 * @return the private key $u_1$ of the Account Holder.
	 */
	Group.Element<G> getPrivateKey();

	/**
	 * @return the public key or identity $I$ of the Account Holder, which
	 *         equals $I = g_1^{u_1}$.
	 */
	Group.Element<G> getPublicKey();

	/**
	 * @return the identity $z$ of the Account Holder blinded by the Bank, which
	 *         equals $z = (I g_2)^x$.
	 */
	Group.Element<G> getBlindedIdentity();

}
