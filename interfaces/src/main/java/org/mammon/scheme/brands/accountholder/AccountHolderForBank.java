package org.mammon.scheme.brands.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolderPrivate must
 * return <code>true</code>.
 */
public interface AccountHolderForBank<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AccountHolder<G, F, T, H, H0> {

	/**
	 * @return the public key or identity $I$ of the Account Holder, which
	 *         equals $I = g_1^{u_1}$.
	 */
	Group.Element<G> getPublicKey();

}