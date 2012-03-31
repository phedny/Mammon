package org.mammon.scheme.brands.coin;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolder;

/**
 * In the Brands scheme, coins that have been issued, but have not been spent
 * yet, are hold by an bearer that must be an AccountHolderPrivate. The
 * isSellable() method of any implementation of this interface must return
 * <code>true</code>.
 */
public interface UnspentCoin<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>>
		extends Coin<G, F, S, T, H, H0> {

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	@Override
	AccountHolder<G, F, S, T, H, H0> getBearer();

	/**
	 * @return the identity blinding factor $s$.
	 */
	FiniteField.Element<F> getBlindingFactor();

	/**
	 * @return the payer witnesses $x_1$.
	 */
	FiniteField.Element<F> getPayerWitness1();

	/**
	 * @return the payer witnesses $x_2$.
	 */
	FiniteField.Element<F> getPayerWitness2();

}
