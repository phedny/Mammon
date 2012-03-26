package org.mammon.brands;

/**
 * In the Brands scheme, coins that have been issued and spent, are hold by an
 * bearer that must be a Shop. The isSellable() method of any implementation of
 * this interface must return <code>false</code>.
 */
public interface SpentCoin extends Coin {

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	@Override
	Shop getBearer();

	/**
	 * @return the time when the payment protocol of the Brands scheme has been
	 *         executed.
	 */
	Void getTime();

	/**
	 * @return array of length 2, containing $r_1$ and $r_2$, which can be used
	 *         to prove that the coin has been spent by the original
	 *         AccountHolder.
	 */
	Void[] getSpendingCommitments();

}
