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
 * @param <S>
 *            The type used to identify shops.
 * @param <T>
 *            The type used to express times.
 * @param <H>
 *            The signature hash function.
 * @param <H0>
 *            The payment hash function.
 */
public interface BrandsSchemeSetup<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>> {

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
	 * @return an array of length 3, containing the generators that are named
	 *         $g$, $g_1$ and $g_2$ in the Brands scheme.
	 */
	Group.Element<G>[] getGenerators();

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
