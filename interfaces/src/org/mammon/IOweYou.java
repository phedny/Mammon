package org.mammon;

/**
 * The IOweYou interface represents an IOU in the most abstract level.
 */
public interface IOweYou {

	/**
	 * The issuer of an IOU is the entity that has created the promise to pay
	 * the face value when a bearer wants to redeem the IOU.
	 * 
	 * @return the issuer of this IOU.
	 */
	Issuer getIssuer();

	/**
	 * The bearer of an IOU is the entity that currently holds the IOU and is
	 * therefore in the position to redeem it.
	 * 
	 * @return the current bearer of this IOU.
	 */
	Bearer getBearer();

	/**
	 * @return the asset type of this IOU.
	 */
	AssetType getAssetType();

	/**
	 * @return the face value (amount owed) of this IOU.
	 */
	Number getFaceValue();

	/**
	 * Determine whether this IOU can be sold, such that it will be hold by
	 * another bearer.
	 * 
	 * @return <code>true</code> iff this IOU can be sold, <code>false</code>
	 *         otherwise.
	 */
	boolean isSellable();

}
