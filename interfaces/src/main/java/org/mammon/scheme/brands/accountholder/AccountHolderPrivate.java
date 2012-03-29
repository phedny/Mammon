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
public interface AccountHolderPrivate<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>>
		extends AccountHolderForBank<G, F, S, T, H, H0> {

	/**
	 * @return the private key $u_1$ of the Account Holder.
	 */
	FiniteField.Element<F> getPrivateKey();

}
