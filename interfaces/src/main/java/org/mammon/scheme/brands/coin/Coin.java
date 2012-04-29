package org.mammon.scheme.brands.coin;

import org.mammon.IOweYou;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.Bank;

public interface Coin<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends IOweYou {

	/**
	 * @return the setup instantiation used by this object.
	 */
	BrandsSchemeSetup<G, F, T, H, H0> getSetup();

	/**
	 * The issuer of an IOU is the entity that has created the promise to pay
	 * the face value when a bearer wants to redeem the IOU.
	 * 
	 * @return the issuer of this IOU.
	 */
	@Override
	Bank<G, F, T, H, H0> getIssuer();

	/**
	 * @return the blinded identity $A$ of the AccountHolderPrivate that
	 *         withdrew the coins from the BankPrivate.
	 */
	Group.Element<G> getBlindedIdentity();

	/**
	 * @return the commitment $B$ to the secret values of the coin known to the
	 *         AccountHolderPrivate.
	 */
	Group.Element<G> getCommitment();

	/**
	 * @return and array of length 4, which represents the signature
	 *         $\operatorname(Sig){A,B}$, which can be used to verify that this
	 *         coin has been correctly issued by the BankPrivate and must
	 *         therefore be valid.
	 */
	CoinSignature<G, F> getCoinSignature();

	/**
	 * Verify whether the signature on this coin is valid.
	 * 
	 * @return <code>true</code> if and only if the signature of this coin is
	 *         valid, <code>false</code> otherwise.
	 */
	boolean verifyCoinSignature();

}