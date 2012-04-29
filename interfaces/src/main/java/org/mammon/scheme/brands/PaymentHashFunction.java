package org.mammon.scheme.brands;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;

/**
 * The payment hash is used to compute challenges during the payment protocol.
 */
public interface PaymentHashFunction<G extends Group<G>, F extends FiniteField<F>, T> {

	/**
	 * Hash the input value into an output element.
	 * 
	 * @param blindedIdentity
	 *            the blinded identity $A$ of the AccountHolderPrivate that
	 *            withdrew the coins from the BankPrivate.
	 * @param commitment
	 *            the commitment $B$ to the secret values of the coin known to
	 *            the AccountHolderPrivate.
	 * @param shopIdentity
	 *            identity of the shop that accepted the coin.
	 * @param time
	 *            time when the payment protocol has been executed.
	 * @return hashed value.
	 */
	FiniteField.Element<F> hash(Group.Element<G> blindedIdentity,
			Group.Element<G> commitment, String shopIdentity, T time);

}
