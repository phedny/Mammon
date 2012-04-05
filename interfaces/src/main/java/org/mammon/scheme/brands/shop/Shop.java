package org.mammon.scheme.brands.shop;

import org.mammon.Bearer;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, one type of bearer is the generic shop. The
 * isSellable() method on IOweYou objects hold by an Shop must return
 * <code>true</code>.
 */
public interface Shop<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends Bearer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, F, I, T, H, H0> getSetup();

	/**
	 * @return the identity $I_\mathcal{S}$ of the shop.
	 */
	String getIdentity();

}
