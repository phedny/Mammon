package org.mammon.brands;

import org.mammon.IOweYou;

public interface Coin extends IOweYou {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup getSetup();

	/**
	 * The issuer of an IOU is the entity that has created the promise to pay
	 * the face value when a bearer wants to redeem the IOU.
	 * 
	 * @return the issuer of this IOU.
	 */
	@Override
	Bank getIssuer();

	/**
	 * @return the blinded identity $A$ of the AccountHolder that withdrew the
	 *         coins from the Bank.
	 */
	Void getBlindedIdentity();

	/**
	 * @return the commitment $B$ to the secret values of the coin known to the
	 *         AccountHolder.
	 */
	Void getCommitment();

	/**
	 * @return the signature $\operatorname(Sig){A,B}$, which can be used to
	 *         verify that this coin has been correctly issued by the Bank and
	 *         must therefore be valid.
	 */
	Void getCoinSignature();

}