package org.mammon.brands;

import java.util.Random;

/**
 * This interface can be used for generic typing, to bind classes of an actual
 * implementation compile-time.
 * 
 * The group must comply to the requirements set out in the Brands scheme for
 * the group $G$ and may in particular be $\mathbb{Z}_q^*$.
 */
public interface Group<G extends Group<G>> {

	/**
	 * @return the zero element of this group.
	 */
	Element<G> getZero();

	/**
	 * @return the unit (one) element of this group.
	 */
	Element<G> getOne();

	/**
	 * @param random
	 *            randomization source.
	 * @return a random element from this group.
	 */
	Element<G> getRandomElement(Random random);

	/**
	 * This interface represents an element from a group.
	 */
	interface Element<G> {

		/**
		 * @return the group this element belongs to.
		 */
		G getGroup();

		/**
		 * @return the inverse of this element.
		 */
		Element<G> getInverse();

		/**
		 * @param other
		 *            an element from the same group to add together.
		 * @return the sum of this element and the other element.
		 */
		Element<G> add(Element<G> other);

		/**
		 * @param other
		 *            an element from the same group to multiply together.
		 * @return the multiplication of this element and the other element.
		 */
		Element<G> multiply(Element<G> other);

		/**
		 * @param exponent
		 *            an element from the same group to use as exponent.
		 * @return the exponentiation of this element by the exponent.
		 */
		Element<G> exponentiate(Element<G> exponent);

	}

}
