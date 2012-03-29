package org.mammon.scheme.brands.accountholder;

import org.mammon.Bearer;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolderPrivate must
 * return <code>true</code>.
 */
public interface AccountHolder<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>>
		extends Bearer {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, F, S, T, H, H0> getSetup();

	/**
	 * @return the identity $z$ of the Account Holder blinded by the
	 *         BankPrivate, which equals $z = (I g_2)^x$.
	 */
	Group.Element<G> getBlindedIdentity();

}