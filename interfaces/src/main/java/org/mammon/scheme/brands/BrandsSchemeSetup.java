package org.mammon.scheme.brands;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;

/**
 * Any object that implements this interface represents a concrete setup of the
 * Brands scheme. All objects that refer to the same concrete setup are
 * compatible during transactions.
 * 
 * @param <G>
 *            The group used for this setup.
 * @param <T>
 *            The type used to express times.
 * @param <H>
 *            The signature hash function.
 * @param <H0>
 *            The payment hash function.
 */
public interface BrandsSchemeSetup<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>> {

	/**
	 * @return the object that describes the group that is named $G$ in the
	 *         Brands scheme.
	 */
	G getGroup();

	/**
	 * @return the object that describes the finite field that is named
	 *         $\mathbb{Z}_p^*$ in the Brands scheme.
	 */
	F getFiniteField();

	/**
	 * @param i the index of the generator to obtains, must be 0, 1 or 2.
	 * @return the generator with the reqeusted index.
	 */
	Group.Element<G> getGenerator(int i);

	/**
	 * @return the hash function used to construct and verify signatures of the
	 *         BankPrivate. In the Brands scheme, this function is named
	 *         $\mathcal{H}$.
	 */
	H getSignatureHash();

	/**
	 * @return the hash function used to compute challenges during the payment
	 *         protocol. In the Brands scheme, this function is named
	 *         $\mathcal{H}_0$.
	 */
	H0 getPaymentHash();

}
