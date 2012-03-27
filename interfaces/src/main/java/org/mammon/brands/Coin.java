package org.mammon.brands;

import org.mammon.IOweYou;

public interface Coin<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		extends IOweYou {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, S, T, H, H0> getSetup();

	/**
	 * The issuer of an IOU is the entity that has created the promise to pay
	 * the face value when a bearer wants to redeem the IOU.
	 * 
	 * @return the issuer of this IOU.
	 */
	@Override
	Bank<G, S, T, H, H0> getIssuer();

	/**
	 * @return the blinded identity $A$ of the AccountHolder that withdrew the
	 *         coins from the Bank.
	 */
	Group.Element<G> getBlindedIdentity();

	/**
	 * @return the commitment $B$ to the secret values of the coin known to the
	 *         AccountHolder.
	 */
	Group.Element<G> getCommitment();

	/**
	 * @return and array of length 4, which represents the signature
	 *         $\operatorname(Sig){A,B}$, which can be used to verify that this
	 *         coin has been correctly issued by the Bank and must therefore be
	 *         valid.
	 */
	Group.Element<G>[] getCoinSignature();

}