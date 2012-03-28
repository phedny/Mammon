package org.mammon.scheme.brands.bank;

import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, an issuer of an IOU is called the bank.
 */
public interface BankPrivate<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends Bank<G, S, T, H, H0> {

	/**
	 * @return the private key $x$ of the BankPrivate.
	 */
	Group.Element<G> getPrivateKey();

}
