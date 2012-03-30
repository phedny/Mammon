package org.mammon.scheme.brands.coin;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;

/**
 * This interface implements the signature of a coin. Together with the blinded
 * identity and commitment of a coin and also the public key of the bank, anyone
 * can verify the validity of the coin signature.
 */
public interface CoinSignature<G extends Group<G>, F extends FiniteField<F>> {

	/**
	 * @return the blinded identity of the account holder.
	 */
	Group.Element<G> getValZ();

	/**
	 * @return the 'a' value of the signature.
	 */
	Group.Element<G> getValA();

	/**
	 * @return the 'b' value of the signature.
	 */
	Group.Element<G> getValB();

	/**
	 * @return the part of the signature that has been committed to by the bank.
	 */
	FiniteField.Element<F> getValR();

}
