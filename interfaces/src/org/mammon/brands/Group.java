package org.mammon.brands;

/**
 * This interface can be used for generic typing, to bind classes of an actual
 * implementation compile-time.
 * 
 * The group must comply to the requirements set out in the Brands scheme for
 * the group $G$ and may in particular be $\mathbb{Z}_q^*$.
 */
public interface Group {

	/**
	 * This interface represents an element from a group.
	 * 
	 * @param <G>
	 */
	interface Element<G extends Group> {
		// Empty
	}

}
