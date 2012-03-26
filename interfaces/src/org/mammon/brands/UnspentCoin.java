package org.mammon.brands;

/**
 * In the Brands scheme, coins that have been issued, but have not been spent
 * yet, are hold by an bearer that must be an AccountHolder. The isSellable()
 * method of any implementation of this interface must return <code>true</code>.
 */
public interface UnspentCoin extends Coin {

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	@Override
	AccountHolder getBearer();

	/**
	 * @return the identity blinding factor $s$, which is an element of the
	 *         group getSetup().getGroup().
	 */
	Void getBlindingFactor();

	/**
	 * @return an array of length 2, containing the payer witnesses $x_1$ and
	 *         $x_2$, which are elements of the group getSetup().getGroup().
	 */
	Void[] getPayerWitness();

}
