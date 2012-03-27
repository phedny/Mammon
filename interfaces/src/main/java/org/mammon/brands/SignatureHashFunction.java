package org.mammon.brands;

/**
 * The signature hash is used to construct and verify signatures of the Bank.
 */
public interface SignatureHashFunction<G extends Group<G>> {

	/**
	 * Hash the input value into an output element.
	 * 
	 * @param blindedIdentity
	 *            the blinded identity $A$ of the AccountHolder that withdrew
	 *            the coins from the Bank.
	 * @param commitment
	 *            the commitment $B$ to the secret values of the coin known to
	 *            the AccountHolder.
	 * @param secretValues
	 *            array of length 3 containing the secret values $z'$, $a'$ and
	 *            $b'$.
	 * @return hashed value.
	 */
	Group.Element<G> hash(Group.Element<G> blindedIdentity, Group.Element<G> commitment,
			Group.Element<G>... secretValues);

}
