package org.mammon.scheme.brands.coin;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.shop.Shop;

/**
 * In the Brands scheme, coins that have been issued and spent, are hold by an
 * bearer that must be a Shop. The isSellable() method of any implementation of
 * this interface must return <code>false</code>.
 */
public interface SpentCoin<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends Coin<G, F, T, H, H0> {

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	@Override
	Shop<G, F, T, H, H0> getBearer();

	/**
	 * @return the time when the payment protocol of the Brands scheme has been
	 *         executed.
	 */
	T getTime();

	/**
	 * @return the first spending commitment $r_1$, which can be used to prove
	 *         that the coin has been spent by the original
	 *         AccountHolderPrivate.
	 */
	FiniteField.Element<F> getR1();

	/**
	 * @return the first spending commitment $r_2$, which can be used to prove
	 *         that the coin has been spent by the original
	 *         AccountHolderPrivate.
	 */
	FiniteField.Element<F> getR2();

}
