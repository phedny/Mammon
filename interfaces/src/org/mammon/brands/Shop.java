package org.mammon.brands;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the generic shop. The
 * isSellable() method on IOweYou objects hold by an Shop must return
 * <code>true</code>.
 */
public interface Shop extends Bearer {

	/**
	 * @return the identity $I_\mathcal{S}$ of the shop.
	 */
	Void getIdentity();

}
