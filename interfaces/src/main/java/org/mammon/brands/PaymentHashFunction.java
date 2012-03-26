package org.mammon.brands;

/**
 * The payment hash is used to compute challenges during the payment protocol.
 */
public interface PaymentHashFunction<G extends Group<G>, S, T> {

	/**
	 * Hash the input value into an output element.
	 * 
	 * @param blindedIdentity
	 *            the blinded identity $A$ of the AccountHolder that withdrew
	 *            the coins from the Bank.
	 * @param commitment
	 *            the commitment $B$ to the secret values of the coin known to
	 *            the AccountHolder.
	 * @param shopIdentity
	 *            identity of the shop that accepted the coin.
	 * @param time
	 *            time when the payment protocol has been executed.
	 * @return hashed value.
	 */
	Group.Element<G> hash(Group.Element<G> blindedIdentity, Group.Element<G> commitment, S shopIdentity, T time);

}
