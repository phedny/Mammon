package org.mammon.brands;

/**
 * Any object that implements this interface represents a concrete setup of the
 * Brands scheme. All objects that refer to the same concrete setup are
 * compatible during transactions.
 */
public interface BrandsSchemeSetup {

	/**
	 * @return the object that describes the group that is named $G$ in the
	 *         Brands scheme.
	 */
	Void getGroup();

	/**
	 * @return an array of length 3, containing the generators that are named
	 *         $g$, $g_1$ and $g_2$ in the Brands scheme.
	 */
	Void[] getGenerators();

	/**
	 * @return the hash function used to construct and verify signatures of the
	 *         Bank. In the Brands scheme, this function is named $\mathcal{H}$.
	 */
	Void getSignatureHash();

	/**
	 * @return the hash function used to compute challenges during the payment
	 *         protocol. In the Brands scheme, this function is named
	 *         $\mathcal{H}_0$.
	 */
	Void getPaymentHash();

}
