package org.mammon.scheme.brands.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

/**
 * In the Brands scheme, an issuer of an IOU is called the bank.
 */
public interface BankPrivate<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>>
		extends Bank<G, F, S, T, H, H0> {

	/**
	 * @return the private key $x$ of the BankPrivate.
	 */
	FiniteField.Element<F> getPrivateKey();

}
