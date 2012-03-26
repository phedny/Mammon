package org.mammon.brands;

import java.util.concurrent.Callable;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the generic shop. The
 * isSellable() method on IOweYou objects hold by an Shop must return
 * <code>true</code>.
 */
public interface Shop<G extends Group, S, T, H extends Callable<Group.Element<G>[]>, H0 extends Callable<Group.Element<G>>>
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
