package org.mammon.brands;

import org.mammon.Bearer;

/**
 * In the Brands scheme, one type of bearer is the account holder. The
 * isSellable() method on IOweYou objects hold by an AccountHolder must return
 * <code>true</code>.
 */
public interface AccountHolder extends Bearer {
	// Empty
}
