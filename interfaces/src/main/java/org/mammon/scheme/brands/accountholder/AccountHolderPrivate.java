package org.mammon.scheme.brands.accountholder;

import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolderPrivate must
 * return <code>true</code>.
 */
public interface AccountHolderPrivate<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends AccountHolderForBank<G, S, T, H, H0> {

	/**
	 * @return the private key $u_1$ of the Account Holder.
	 */
	Group.Element<G> getPrivateKey();

}
