package org.mammon.brands;

public interface IOweYou extends org.mammon.IOweYou {

	/**
	 * The issuer of an IOU is the entity that has created the promise to pay
	 * the face value when a bearer wants to redeem the IOU.
	 * 
	 * @return the issuer of this IOU.
	 */
	Bank getIssuer();

}
