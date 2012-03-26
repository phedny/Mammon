package org.mammon;

/**
 * The AssetType interface is used to represent the type of an asset.
 */
public interface AssetType {

	/**
	 * The call sign of an asset type is a short-hand value that can be
	 * immediately recognized by humans and machines. Common call signs include
	 * "USD" for US dollars and "EUR" for Euro.
	 * 
	 * @return the call sign of this asset type.
	 */
	String getCallSign();

}
