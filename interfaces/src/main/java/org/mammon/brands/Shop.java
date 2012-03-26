package org.mammon.brands;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the generic shop. The
 * isSellable() method on IOweYou objects hold by an Shop must return
 * <code>true</code>.
 */
public interface Shop<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends Bearer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, S, T, H, H0> getSetup();

	/**
	 * @return the identity $I_\mathcal{S}$ of the shop.
	 */
	S getIdentity();

}
