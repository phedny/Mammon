package org.mammon.brands;

/**
 * In the Brands scheme, coins that have been issued, but have not been spent
 * yet, are hold by an bearer that must be an AccountHolder. The isSellable()
 * method of any implementation of this interface must return <code>true</code>.
 */
public interface UnspentCoin<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends Coin<G, S, T, H, H0> {

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	@Override
	AccountHolder<G, S, T, H, H0> getBearer();

	/**
	 * @return the identity blinding factor $s$.
	 */
	Group.Element<G> getBlindingFactor();

	/**
	 * @return an array of length 2, containing the payer witnesses $x_1$ and
	 *         $x_2$.
	 */
	Group.Element<G>[] getPayerWitness();

}
