package org.mammon.scheme.brands;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;

/**
 * The signature hash is used to construct and verify signatures of the
 * BankPrivate.
 */
public interface SignatureHashFunction<G extends Group<G>, F extends FiniteField<F>> {

	/**
	 * Hash the input value into an output element.
	 * 
	 * @param blindedIdentity
	 *            the blinded identity $A$ of the AccountHolderPrivate that
	 *            withdrew the coins from the BankPrivate.
	 * @param commitment
	 *            the commitment $B$ to the secret values of the coin known to
	 *            the AccountHolderPrivate.
	 * @param secretValues
	 *            array of length 3 containing the secret values $z'$, $a'$ and
	 *            $b'$.
	 * @return hashed value.
	 */
	FiniteField.Element<F> hash(Group.Element<G> blindedIdentity,
			Group.Element<G> commitment, Group.Element<G>... secretValues);

}
